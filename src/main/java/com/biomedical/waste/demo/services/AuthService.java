package com.biomedical.waste.demo.services;

import com.biomedical.waste.demo.models.User;
import com.biomedical.waste.demo.repository.UserRepository;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Base64;
import java.util.Map;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder(12);

    @Value("${app.jwt.secret:biomedical-waste-secret-key-change-in-production}")
    private String jwtSecret;

    @Value("${app.jwt.expiration-hours:24}")
    private int expirationHours;

    public record AuthResponse(String token, String id, String email, String fullName, String role) {}

    public AuthResponse register(String email, String password, String fullName) {
        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("El correo es obligatorio.");
        }
        validatePassword(password);
        if (userRepository.existsByEmail(email.trim().toLowerCase())) {
            throw new IllegalArgumentException("Ese correo ya está registrado.");
        }

        User user = User.builder()
            .email(email.trim().toLowerCase())
            .passwordHash(passwordEncoder.encode(password))
            .fullName(fullName != null ? fullName.trim() : "Usuario")
            .role("OPERATOR")
            .build();

        user = userRepository.save(user);
        String token = generateToken(user);
        return new AuthResponse(token, user.getId(), user.getEmail(), user.getFullName(), user.getRole());
    }

    public AuthResponse login(String email, String password) {
        if (email == null || email.isBlank() || password == null || password.isBlank()) {
            throw new IllegalArgumentException("Correo y contraseña son obligatorios.");
        }

        User user = userRepository.findByEmail(email.trim().toLowerCase())
            .orElseThrow(() -> new IllegalArgumentException("Credenciales inválidas."));

        if (!passwordEncoder.matches(password, user.getPasswordHash())) {
            throw new IllegalArgumentException("Credenciales inválidas.");
        }

        String token = generateToken(user);
        return new AuthResponse(token, user.getId(), user.getEmail(), user.getFullName(), user.getRole());
    }

    public User validateToken(String token) {
        try {
            String[] parts = token.split("\\.");
            if (parts.length != 3) return null;

            String payloadJson = new String(Base64.getUrlDecoder().decode(parts[1]), StandardCharsets.UTF_8);

            // Verify signature
            String dataToSign = parts[0] + "." + parts[1];
            String expectedSig = hmacSha256(dataToSign);
            if (!expectedSig.equals(parts[2])) return null;

            // Parse expiration
            // Simple JSON parsing for "exp" field
            int expIdx = payloadJson.indexOf("\"exp\":");
            if (expIdx < 0) return null;
            String expStr = payloadJson.substring(expIdx + 6).replaceAll("[^0-9]", "").substring(0, 10);
            long exp = Long.parseLong(expStr);
            if (Instant.now().getEpochSecond() > exp) return null;

            // Parse email
            int emailIdx = payloadJson.indexOf("\"sub\":\"");
            if (emailIdx < 0) return null;
            String emailPart = payloadJson.substring(emailIdx + 7);
            String email = emailPart.substring(0, emailPart.indexOf("\""));

            return userRepository.findByEmail(email).orElse(null);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Validates password strength:
     * - Minimum 8 characters
     * - At least one uppercase letter
     * - At least one lowercase letter
     * - At least one digit
     * - At least one special character
     */
    private void validatePassword(String password) {
        if (password == null || password.length() < 8) {
            throw new IllegalArgumentException("La contraseña debe tener al menos 8 caracteres.");
        }
        if (password.length() > 22) {
            throw new IllegalArgumentException("La contraseña no puede tener más de 22 caracteres.");
        }
        if (!password.matches(".*[A-Z].*")) {
            throw new IllegalArgumentException("La contraseña debe contener al menos una letra mayúscula.");
        }
        if (!password.matches(".*[a-z].*")) {
            throw new IllegalArgumentException("La contraseña debe contener al menos una letra minúscula.");
        }
        if (!password.matches(".*\\d.*")) {
            throw new IllegalArgumentException("La contraseña debe contener al menos un número.");
        }
        if (!password.matches(".*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>/?].*")) {
            throw new IllegalArgumentException("La contraseña debe contener al menos un carácter especial (!@#$%^&*...).");
        }
    }

    private String generateToken(User user) {
        long now = Instant.now().getEpochSecond();
        long exp = Instant.now().plus(expirationHours, ChronoUnit.HOURS).getEpochSecond();

        String header = base64Url("""
            {"alg":"HS256","typ":"JWT"}""".strip());

        String payload = base64Url(String.format(
            """
            {"sub":"%s","role":"%s","name":"%s","iat":%d,"exp":%d}""".strip(),
            user.getEmail(), user.getRole(), user.getFullName(), now, exp
        ));

        String signature = hmacSha256(header + "." + payload);
        return header + "." + payload + "." + signature;
    }

    private String hmacSha256(String data) {
        try {
            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(new SecretKeySpec(jwtSecret.getBytes(StandardCharsets.UTF_8), "HmacSHA256"));
            byte[] hash = mac.doFinal(data.getBytes(StandardCharsets.UTF_8));
            return Base64.getUrlEncoder().withoutPadding().encodeToString(hash);
        } catch (Exception e) {
            throw new RuntimeException("Error generating JWT signature", e);
        }
    }

    private String base64Url(String input) {
        return Base64.getUrlEncoder().withoutPadding().encodeToString(input.getBytes(StandardCharsets.UTF_8));
    }
}

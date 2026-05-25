package com.biomedical.waste.demo.controllers;

import com.biomedical.waste.demo.services.AuthService;
import com.biomedical.waste.demo.services.AuthService.AuthResponse;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    /**
     * POST /api/auth/register
     * Body: { "email": "...", "password": "...", "fullName": "..." }
     * Returns: { "token": "...", "id": "...", "email": "...", "fullName": "...", "role": "..." }
     */
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody Map<String, String> body) {
        try {
            String email = body.get("email");
            String password = body.get("password");
            String fullName = body.getOrDefault("fullName", "Usuario");

            AuthResponse response = authService.register(email, password, fullName);
            return ResponseEntity.status(HttpStatus.CREATED).body(Map.of(
                "token", response.token(),
                "user", Map.of(
                    "id", response.id(),
                    "email", response.email(),
                    "fullName", response.fullName(),
                    "role", response.role()
                )
            ));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        }
    }

    /**
     * POST /api/auth/login
     * Body: { "email": "...", "password": "..." }
     * Returns: { "token": "...", "user": { "id", "email", "fullName", "role" } }
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> body) {
        try {
            String email = body.get("email");
            String password = body.get("password");

            AuthResponse response = authService.login(email, password);
            return ResponseEntity.ok(Map.of(
                "token", response.token(),
                "user", Map.of(
                    "id", response.id(),
                    "email", response.email(),
                    "fullName", response.fullName(),
                    "role", response.role()
                )
            ));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message", e.getMessage()));
        }
    }
}

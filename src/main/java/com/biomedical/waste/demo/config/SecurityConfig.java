package com.biomedical.waste.demo.config;

import java.nio.charset.StandardCharsets;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.core.OAuth2TokenValidator;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtValidators;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    @Value("${supabase.project.ref:}")
    private String supabaseProjectRef;

    @Value("${supabase.jwt.secret:}")
    private String supabaseJwtSecret;

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(AbstractHttpConfigurer::disable)
            .cors(Customizer.withDefaults())
            .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth
                .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                .requestMatchers("/api/chat/**").permitAll()
                .requestMatchers("/api/statistics/**").permitAll()
                .requestMatchers("/api/routes/**").permitAll()
                .requestMatchers("/api/wastes/**").permitAll()
                .requestMatchers("/api/alerts/**").permitAll()
                .requestMatchers("/api/admin/**").authenticated()
                .anyRequest().permitAll()
            )
            .oauth2ResourceServer(oauth2 -> oauth2.jwt(Customizer.withDefaults()))
            .httpBasic(AbstractHttpConfigurer::disable)
            .formLogin(AbstractHttpConfigurer::disable);

        return http.build();
    }

    @Bean
    JwtDecoder jwtDecoder() {
        String ref = supabaseProjectRef == null ? "" : supabaseProjectRef.trim();
        String secret = supabaseJwtSecret == null ? "" : supabaseJwtSecret.trim();

        NimbusJwtDecoder decoder;

        if (!secret.isBlank()) {
            SecretKey key = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
            decoder = NimbusJwtDecoder.withSecretKey(key).macAlgorithm(MacAlgorithm.HS256).build();
        } else {
            if (ref.isBlank()) {
                throw new IllegalStateException("Falta configurar SUPABASE_PROJECT_REF o SUPABASE_JWT_SECRET");
            }
            String jwkSetUri = "https://" + ref + ".supabase.co/auth/v1/.well-known/jwks.json";
            decoder = NimbusJwtDecoder.withJwkSetUri(jwkSetUri).build();
        }

        String issuer = ref.isBlank() ? null : ("https://" + ref + ".supabase.co/auth/v1");
        OAuth2TokenValidator<Jwt> validator = issuer == null ? JwtValidators.createDefault() : JwtValidators.createDefaultWithIssuer(issuer);
        decoder.setJwtValidator(validator);
        return decoder;
    }
}

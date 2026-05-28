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
import com.biomedical.waste.demo.security.AdminKeyRequiredFilter;
import com.biomedical.waste.demo.security.RestAccessDeniedHandler;
import com.biomedical.waste.demo.security.RestAuthenticationEntryPoint;
import org.springframework.security.oauth2.server.resource.web.BearerTokenAuthenticationFilter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import org.springframework.core.annotation.Order;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final AdminKeyRequiredFilter adminKeyRequiredFilter;
    private final RestAuthenticationEntryPoint restAuthenticationEntryPoint;
    private final RestAccessDeniedHandler restAccessDeniedHandler;

    @Value("${supabase.project.ref:}")
    private String supabaseProjectRef;

    @Value("${supabase.jwt.secret:}")
    private String supabaseJwtSecret;

    @Bean
    @Order(1)
    SecurityFilterChain adminFilterChain(HttpSecurity http) throws Exception {
        http
            .securityMatcher("/api/admin/**")
            .csrf(AbstractHttpConfigurer::disable)
            .cors(Customizer.withDefaults())
            .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .exceptionHandling(ex -> ex
                .authenticationEntryPoint(restAuthenticationEntryPoint)
                .accessDeniedHandler(restAccessDeniedHandler)
            )
            .addFilterAfter(adminKeyRequiredFilter, BearerTokenAuthenticationFilter.class)
            .authorizeHttpRequests(auth -> auth
                .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                .anyRequest().authenticated()
            )
            .oauth2ResourceServer(oauth2 -> oauth2.jwt(Customizer.withDefaults()))
            .httpBasic(AbstractHttpConfigurer::disable)
            .formLogin(AbstractHttpConfigurer::disable);

        return http.build();
    }

    @Bean
    @Order(2)
    SecurityFilterChain publicFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(AbstractHttpConfigurer::disable)
            .cors(Customizer.withDefaults())
            .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth
                .anyRequest().permitAll()
            )
            .httpBasic(AbstractHttpConfigurer::disable)
            .formLogin(AbstractHttpConfigurer::disable);

        return http.build();
    }

    @Bean
    JwtDecoder jwtDecoder() {
        String ref = supabaseProjectRef == null ? "" : supabaseProjectRef.trim();
        String secret = supabaseJwtSecret == null ? "" : supabaseJwtSecret.trim();

        if (secret.isBlank() && ref.isBlank()) {
            // Returns a dummy decoder to prevent the application from crashing on startup
            // if Supabase environment variables are not configured.
            return token -> {
                throw new org.springframework.security.oauth2.jwt.JwtValidationException(
                    "Supabase not configured in the backend",
                    java.util.List.of(new org.springframework.security.oauth2.core.OAuth2Error("config_missing"))
                );
            };
        }

        NimbusJwtDecoder decoder;

        if (!secret.isBlank()) {
            SecretKey key = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
            decoder = NimbusJwtDecoder.withSecretKey(key).macAlgorithm(MacAlgorithm.HS256).build();
        } else {
            String jwkSetUri = "https://" + ref + ".supabase.co/auth/v1/.well-known/jwks.json";
            decoder = NimbusJwtDecoder.withJwkSetUri(jwkSetUri).build();
        }

        String issuer = ref.isBlank() ? null : ("https://" + ref + ".supabase.co/auth/v1");
        OAuth2TokenValidator<Jwt> validator = issuer == null ? JwtValidators.createDefault() : JwtValidators.createDefaultWithIssuer(issuer);
        decoder.setJwtValidator(validator);
        return decoder;
    }
}

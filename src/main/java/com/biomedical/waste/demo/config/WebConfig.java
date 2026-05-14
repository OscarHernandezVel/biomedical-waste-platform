package com.biomedical.waste.demo.config;

import java.util.Arrays;
import java.util.stream.Stream;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {
    private final AdminApiKeyInterceptor adminApiKeyInterceptor;

    @Value("${frontend.origin:https://traebiomedical-frontedxit2.vercel.app}")
    private String frontendOrigin;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(adminApiKeyInterceptor).addPathPatterns("/api/admin/**");
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        String[] origins = Stream.concat(
                Arrays.stream(frontendOrigin.split(",")).map(String::trim).filter(s -> !s.isBlank()),
                Stream.of("http://localhost:5173", "http://localhost:3000")
            )
            .distinct()
            .toArray(String[]::new);

        registry.addMapping("/**")
            .allowedOrigins(origins)
            .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
            .allowedHeaders("*")
            .exposedHeaders("*");
    }
}

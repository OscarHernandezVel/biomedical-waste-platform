package com.biomedical.waste.demo.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class AdminApiKeyInterceptor implements HandlerInterceptor {

    @Value("${admin.api.key:}")
    private String adminApiKey;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // Permitir preflight de CORS sin auth
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            return true;
        }

        if (adminApiKey == null || adminApiKey.isBlank()) {
            return true;
        }
        String provided = request.getHeader("X-Admin-Key");
        if (provided != null && provided.equals(adminApiKey)) {
            return true;
        }
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType("application/json");
        response.getWriter().write("{\"message\":\"No autorizado\"}");
        return false;
    }
}


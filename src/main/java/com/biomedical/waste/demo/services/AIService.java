package com.biomedical.waste.demo.services;

import com.biomedical.waste.demo.models.AlertLevel;
import com.biomedical.waste.demo.models.ChatMessage;
import com.biomedical.waste.demo.models.ChatRequest;
import com.biomedical.waste.demo.models.ChatResponse;
import com.biomedical.waste.demo.repository.AlertRepository;
import com.biomedical.waste.demo.repository.WasteRepository;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class AIService {

    private static final String GEMINI_URL = "https://generativelanguage.googleapis.com/v1beta/models/%s:generateContent?key=%s";

    private final WasteRepository wasteRepository;
    private final AlertRepository alertRepository;
    private final RestTemplateBuilder restTemplateBuilder;

    @Value("${gemini.api.key:${GOOGLE_API_KEY:${GEMINI_API_KEY:}}}")
    private String geminiApiKey;

    @Value("${gemini.model:gemini-2.0-flash}")
    private String model;

    @Value("${gemini.max-tokens:1000}")
    private int maxTokens;

    @Value("${gemini.temperature:0.7}")
    private double temperature;

    /** Sends a message to the assistant and returns the model response. */
    public ChatResponse chat(ChatRequest request) {
        if (request == null || request.getMessage() == null || request.getMessage().isBlank()) {
            return ChatResponse.builder()
                .message("El mensaje no puede estar vacío.")
                .success(false)
                .timestamp(LocalDateTime.now().toString())
                .build();
        }

        ChatResponse quick = quickAnswer(request.getMessage());
        if (quick != null) {
            return quick;
        }

        if (geminiApiKey == null || geminiApiKey.isBlank()) {
            return ChatResponse.builder()
                .message(buildOfflineAssistantResponse(request.getMessage()))
                .success(true)
                .timestamp(LocalDateTime.now().toString())
                .build();
        }

        try {
            // Build Gemini request body
            List<Map<String, Object>> contents = new ArrayList<>();

            // System instruction as first user message context
            String systemPrompt = buildSystemPrompt();

            // Add history if present
            if (request.getHistory() != null) {
                for (ChatMessage msg : request.getHistory()) {
                    if (msg != null && msg.getRole() != null && msg.getContent() != null) {
                        String role = msg.getRole().trim().toLowerCase();
                        String geminiRole = "user".equals(role) ? "user" : "model";
                        contents.add(Map.of(
                            "role", geminiRole,
                            "parts", List.of(Map.of("text", msg.getContent()))
                        ));
                    }
                }
            }

            // Add current user message with system context
            String userMessage = systemPrompt + "\n\nUsuario: " + request.getMessage();
            contents.add(Map.of(
                "role", "user",
                "parts", List.of(Map.of("text", userMessage))
            ));

            Map<String, Object> generationConfig = new HashMap<>();
            generationConfig.put("maxOutputTokens", maxTokens);
            generationConfig.put("temperature", temperature);

            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("contents", contents);
            requestBody.put("generationConfig", generationConfig);

            String url = String.format(GEMINI_URL, model, geminiApiKey);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);
            ResponseEntity<Map> response = restTemplateBuilder.build().postForEntity(url, entity, Map.class);

            String content = extractGeminiContent(response.getBody());
            if (content == null || content.isBlank()) {
                return ChatResponse.builder()
                    .message("No se pudo obtener respuesta del asistente.")
                    .success(false)
                    .timestamp(LocalDateTime.now().toString())
                    .build();
            }

            return ChatResponse.builder()
                .message(content.trim())
                .success(true)
                .timestamp(LocalDateTime.now().toString())
                .build();
        } catch (RestClientException e) {
            return ChatResponse.builder()
                .message(buildOfflineAssistantResponse(request.getMessage()))
                .success(true)
                .error(e.getMessage())
                .timestamp(LocalDateTime.now().toString())
                .build();
        }
    }

    /** Analyzes text + image using Gemini multimodal capabilities. */
    public ChatResponse analyzeWithImage(String prompt, MultipartFile image) {
        if (geminiApiKey == null || geminiApiKey.isBlank()) {
            return ChatResponse.builder()
                .message("Análisis de imagen no disponible en modo offline. Configura GEMINI_API_KEY.")
                .success(false)
                .timestamp(LocalDateTime.now().toString())
                .build();
        }

        try {
            byte[] imageBytes = image.getBytes();
            String base64Image = Base64.getEncoder().encodeToString(imageBytes);
            String mimeType = image.getContentType() != null ? image.getContentType() : "image/jpeg";

            // Build multimodal parts: text + image
            List<Map<String, Object>> parts = new ArrayList<>();
            parts.add(Map.of("text", prompt));
            parts.add(Map.of("inlineData", Map.of(
                "mimeType", mimeType,
                "data", base64Image
            )));

            List<Map<String, Object>> contents = new ArrayList<>();
            contents.add(Map.of("role", "user", "parts", parts));

            Map<String, Object> generationConfig = new HashMap<>();
            generationConfig.put("maxOutputTokens", maxTokens);
            generationConfig.put("temperature", temperature);

            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("contents", contents);
            requestBody.put("generationConfig", generationConfig);

            String url = String.format(GEMINI_URL, model, geminiApiKey);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);
            ResponseEntity<Map> response = restTemplateBuilder.build().postForEntity(url, entity, Map.class);

            String content = extractGeminiContent(response.getBody());
            if (content == null || content.isBlank()) {
                return ChatResponse.builder()
                    .message("No se pudo analizar la imagen.")
                    .success(false)
                    .timestamp(LocalDateTime.now().toString())
                    .build();
            }

            return ChatResponse.builder()
                .message(content.trim())
                .success(true)
                .timestamp(LocalDateTime.now().toString())
                .build();
        } catch (Exception e) {
            return ChatResponse.builder()
                .message("Error al analizar la imagen: " + e.getMessage())
                .success(false)
                .error(e.getMessage())
                .timestamp(LocalDateTime.now().toString())
                .build();
        }
    }

    private String buildOfflineAssistantResponse(String userMessage) {
        long totalWastes = wasteRepository.count();
        long activeAlerts = alertRepository.findByResolved(false).size();
        long highRiskAlerts = alertRepository.countByLevel(AlertLevel.HIGH);

        String msg = userMessage == null ? "" : userMessage.trim().toLowerCase();

        String base = "Asistente en modo offline (sin conexión a proveedor de IA).\n" +
            "Datos actuales del sistema:\n" +
            "- Total de residuos registrados: " + totalWastes + "\n" +
            "- Alertas activas sin resolver: " + activeAlerts + "\n" +
            "- Alertas de alto riesgo: " + highRiskAlerts + "\n\n";

        if (msg.contains("residuo") || msg.contains("tipo")) {
            ChatResponse quick = quickAnswer("tipos");
            return base + (quick != null ? quick.getMessage() : "Puedo ayudarte con tipos de residuos y su gestión.") + "\n" +
                "Tip: escribe 'tipos' para ver la lista completa.";
        }

        if (msg.contains("norma") || msg.contains("decreto") || msg.contains("resolución") || msg.contains("resolucion")) {
            ChatResponse quick = quickAnswer("normativa");
            return base + (quick != null ? quick.getMessage() : "Puedo ayudarte con normativa colombiana aplicable.");
        }

        if (msg.contains("trat") || msg.contains("autoclave") || msg.contains("inciner")) {
            ChatResponse quick = quickAnswer("tratamientos");
            return base + (quick != null ? quick.getMessage() : "Puedo ayudarte con tratamientos recomendados según el tipo de residuo.");
        }

        if (msg.contains("alert")) {
            return base + "Recomendación de operación:\n" +
                "- Revisa primero alertas de ALTO riesgo.\n" +
                "- Valida trazabilidad: generación → recolección → tratamiento → disposición final.\n" +
                "- Si una alerta está asociada a una ruta/orden, prioriza su ejecución.";
        }

        if (msg.contains("ruta") || msg.contains("vehiculo") || msg.contains("vehículo") || msg.contains("orden")) {
            return base + "Flujo recomendado:\n" +
                "1) Verifica órdenes pendientes.\n" +
                "2) Asigna ruta y vehículo disponibles.\n" +
                "3) Ejecuta recolección y genera manifiesto.\n" +
                "4) Cierra la orden cuando llegue a planta y se registre disposición final.";
        }

        return base + "Puedo ayudarte con: tipos de residuos, tratamientos, normativa, rutas/órdenes y alertas.\n" +
            "Para respuestas completas con IA, configura `GEMINI_API_KEY` en Railway (backend).";
    }

    /** Returns a predefined answer for common topics or null when not applicable. */
    public ChatResponse quickAnswer(String topic) {
        if (topic == null) {
            return null;
        }
        String t = topic.trim().toLowerCase();
        String answer = switch (t) {
            case "tipos" -> """
                Tipos de residuos biomédicos del sistema:
                - INFECTIOUS (Infeccioso): nivel de riesgo 5
                - SHARPS (Cortopunzante): nivel de riesgo 4
                - CHEMICAL (Químico): nivel de riesgo 4
                - PHARMACEUTICAL (Farmacéutico): nivel de riesgo 3
                - ANATOMICAL (Anatómico): nivel de riesgo 5
                """;
            case "normativa" -> """
                Normativa colombiana aplicable (resumen informativo):
                - Decreto 351 de 2014
                - Resolución 1164 de 2002
                Recomendación: mantener clasificación en el punto de generación y trazabilidad completa (PGIRH).
                """;
            case "tratamientos" -> """
                Métodos de tratamiento referenciados en el sistema:
                - Autoclave: esterilización (p.ej., 134°C por 18 min) para residuos infecciosos
                - Incineración: alta temperatura (p.ej., 850°C) para cortopunzantes y anatómicos
                - Neutralización química: para residuos químicos según procedimiento seguro
                """;
            default -> null;
        };

        if (answer == null) {
            return null;
        }
        return ChatResponse.builder()
            .message(answer)
            .success(true)
            .timestamp(LocalDateTime.now().toString())
            .build();
    }

    private String buildSystemPrompt() {
        long totalWastes = wasteRepository.count();
        long activeAlerts = alertRepository.findByResolved(false).size();
        long highRiskAlerts = alertRepository.countByLevel(AlertLevel.HIGH);

        // Get waste details
        StringBuilder wasteDetails = new StringBuilder();
        var wastes = wasteRepository.findAll();
        if (!wastes.isEmpty()) {
            wasteDetails.append("\n            RESIDUOS REGISTRADOS:\n");
            for (var w : wastes) {
                wasteDetails.append("            - ID: ").append(w.getId())
                    .append(" | Tipo: ").append(w.getType())
                    .append(" | Peso: ").append(w.getWeightKg()).append(" kg")
                    .append(" | Entidad: ").append(w.getOriginEntity())
                    .append(" | Estado: ").append(w.getStatus());
                if (w.getDescription() != null && !w.getDescription().isBlank()) {
                    wasteDetails.append(" | Descripción: ").append(w.getDescription());
                }
                wasteDetails.append("\n");
            }
        }

        // Get alert details
        StringBuilder alertDetails = new StringBuilder();
        var alerts = alertRepository.findByResolved(false);
        if (!alerts.isEmpty()) {
            alertDetails.append("\n            ALERTAS ACTIVAS:\n");
            for (var a : alerts) {
                alertDetails.append("            - Nivel: ").append(a.getLevel())
                    .append(" | Mensaje: ").append(a.getMessage())
                    .append("\n");
            }
        }

        return """
            Eres un asistente especializado en gestión de residuos biomédicos en Colombia.
            Responde siempre en español, con tono profesional y claro.

            CONTEXTO DEL SISTEMA EN TIEMPO REAL:
            - Total de residuos registrados: %d
            - Alertas activas sin resolver: %d
            - Alertas de alto riesgo: %d
            %s%s
            ALCANCE:
            - Tipos de residuos biomédicos, tratamientos, alertas, rutas y logística.
            - Normativa: Decreto 351 de 2014 y Resolución 1164 de 2002 (resumen informativo).

            INSTRUCCIONES:
            - Usa los datos en tiempo real cuando sean relevantes.
            - Cuando te pregunten por residuos registrados, muestra la lista detallada.
            - Si falta información específica, indícalo claramente.
            """.formatted(totalWastes, activeAlerts, highRiskAlerts, wasteDetails.toString(), alertDetails.toString());
    }

    /** Extracts the text content from a Gemini API response. */
    private String extractGeminiContent(Map body) {
        if (body == null) {
            return null;
        }
        Object candidatesObj = body.get("candidates");
        if (!(candidatesObj instanceof List<?> candidates) || candidates.isEmpty()) {
            return null;
        }
        Object first = candidates.get(0);
        if (!(first instanceof Map<?, ?> candidate)) {
            return null;
        }
        Object contentObj = candidate.get("content");
        if (!(contentObj instanceof Map<?, ?> content)) {
            return null;
        }
        Object partsObj = content.get("parts");
        if (!(partsObj instanceof List<?> parts) || parts.isEmpty()) {
            return null;
        }
        Object firstPart = parts.get(0);
        if (!(firstPart instanceof Map<?, ?> part)) {
            return null;
        }
        Object text = part.get("text");
        return text instanceof String s ? s : null;
    }
}

package com.biomedical.waste.demo.services;

import com.biomedical.waste.demo.models.AlertLevel;
import com.biomedical.waste.demo.models.ChatMessage;
import com.biomedical.waste.demo.models.ChatRequest;
import com.biomedical.waste.demo.models.ChatResponse;
import com.biomedical.waste.demo.repository.AlertRepository;
import com.biomedical.waste.demo.repository.WasteRepository;
import java.time.LocalDateTime;
import java.util.ArrayList;
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

@Service
@RequiredArgsConstructor
public class AIService {

    private static final String OPENAI_URL = "https://api.openai.com/v1/chat/completions";

    private final WasteRepository wasteRepository;
    private final AlertRepository alertRepository;
    private final RestTemplateBuilder restTemplateBuilder;

    @Value("${openai.api.key:}")
    private String openaiApiKey;

    @Value("${openai.model:gpt-3.5-turbo}")
    private String model;

    @Value("${openai.max-tokens:1000}")
    private int maxTokens;

    @Value("${openai.temperature:0.7}")
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
        if (openaiApiKey == null || openaiApiKey.isBlank()) {
            return ChatResponse.builder()
                .message("Falta configurar la API key de OpenAI en la variable OPENAI_API_KEY.")
                .success(false)
                .timestamp(LocalDateTime.now().toString())
                .build();
        }

        ChatResponse quick = quickAnswer(request.getMessage());
        if (quick != null) {
            return quick;
        }

        try {
            List<Map<String, String>> messages = new ArrayList<>();
            messages.add(Map.of("role", "system", "content", buildSystemPrompt()));
            if (request.getHistory() != null) {
                for (ChatMessage msg : request.getHistory()) {
                    if (msg != null && msg.getRole() != null && msg.getContent() != null) {
                        messages.add(Map.of("role", msg.getRole(), "content", msg.getContent()));
                    }
                }
            }
            messages.add(Map.of("role", "user", "content", request.getMessage()));

            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("model", model);
            requestBody.put("messages", messages);
            requestBody.put("max_tokens", maxTokens);
            requestBody.put("temperature", temperature);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setBearerAuth(openaiApiKey);

            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);
            ResponseEntity<Map> response = restTemplateBuilder.build().postForEntity(OPENAI_URL, entity, Map.class);

            String content = extractAssistantContent(response.getBody());
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
                .message("Error al conectar con el asistente de IA.")
                .success(false)
                .error(e.getMessage())
                .timestamp(LocalDateTime.now().toString())
                .build();
        }
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

        return """
            Eres un asistente especializado en gestión de residuos biomédicos en Colombia.
            Responde siempre en español, con tono profesional y claro.

            CONTEXTO DEL SISTEMA EN TIEMPO REAL:
            - Total de residuos registrados: %d
            - Alertas activas sin resolver: %d
            - Alertas de alto riesgo: %d

            ALCANCE:
            - Tipos de residuos biomédicos, tratamientos, alertas, rutas y logística.
            - Normativa: Decreto 351 de 2014 y Resolución 1164 de 2002 (resumen informativo).

            INSTRUCCIONES:
            - Usa los datos en tiempo real cuando sean relevantes.
            - Si falta información específica, indícalo claramente.
            """.formatted(totalWastes, activeAlerts, highRiskAlerts);
    }

    private String extractAssistantContent(Map body) {
        if (body == null) {
            return null;
        }
        Object choicesObj = body.get("choices");
        if (!(choicesObj instanceof List<?> choices) || choices.isEmpty()) {
            return null;
        }
        Object first = choices.get(0);
        if (!(first instanceof Map<?, ?> choice)) {
            return null;
        }
        Object messageObj = choice.get("message");
        if (!(messageObj instanceof Map<?, ?> message)) {
            return null;
        }
        Object content = message.get("content");
        return content instanceof String s ? s : null;
    }
}


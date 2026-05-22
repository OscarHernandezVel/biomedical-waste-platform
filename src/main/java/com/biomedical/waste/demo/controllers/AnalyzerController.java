package com.biomedical.waste.demo.controllers;

import com.biomedical.waste.demo.services.AIService;
import com.biomedical.waste.demo.models.ChatRequest;
import com.biomedical.waste.demo.models.ChatResponse;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/analyzer")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class AnalyzerController {

    private final AIService aiService;

    /**
     * Analyzes text/image input using AI and returns structured results.
     * Accepts multipart/form-data with fields: analysisType, text, context, image, language, outputFormat.
     */
    @PostMapping
    public ResponseEntity<Map<String, Object>> analyze(
            @RequestParam("analysisType") String analysisType,
            @RequestParam(value = "text", required = false) String text,
            @RequestParam(value = "context", required = false) String context,
            @RequestParam(value = "image", required = false) MultipartFile image,
            @RequestParam(value = "language", defaultValue = "es") String language,
            @RequestParam(value = "outputFormat", defaultValue = "json") String outputFormat) {

        String prompt = buildAnalyzerPrompt(analysisType, text, context, image);

        ChatRequest chatRequest = new ChatRequest();
        chatRequest.setMessage(prompt);

        ChatResponse chatResponse = aiService.chat(chatRequest);

        Map<String, Object> result = new HashMap<>();
        result.put("title", buildTitle(analysisType));
        result.put("description", chatResponse.getMessage());
        result.put("report", chatResponse.getMessage());
        result.put("recommendations", extractRecommendations(chatResponse.getMessage()));
        result.put("isRelevant", true);
        result.put("relevanceScore", 0.9);
        result.put("relevanceReason", "Análisis generado por IA para: " + analysisType);
        result.put("timestamp", LocalDateTime.now().toString());

        return ResponseEntity.ok(result);
    }

    private String buildAnalyzerPrompt(String analysisType, String text, String context, MultipartFile image) {
        StringBuilder prompt = new StringBuilder();
        prompt.append("Actúa como un analista experto en gestión de residuos biomédicos. ");
        prompt.append("Tipo de análisis solicitado: ").append(analysisType).append(".\n\n");

        if (context != null && !context.isBlank()) {
            prompt.append("Contexto adicional: ").append(context).append("\n\n");
        }

        if (text != null && !text.isBlank()) {
            prompt.append("Texto a analizar:\n").append(text).append("\n\n");
        }

        if (image != null && !image.isEmpty()) {
            prompt.append("[Se adjuntó una imagen: ").append(image.getOriginalFilename()).append("]\n\n");
        }

        prompt.append("Responde en español con:\n");
        prompt.append("1. Un resumen ejecutivo del análisis.\n");
        prompt.append("2. Hallazgos principales.\n");
        prompt.append("3. Recomendaciones concretas (lista numerada).\n");
        prompt.append("4. Nivel de riesgo o relevancia si aplica.\n");

        return prompt.toString();
    }

    private String buildTitle(String analysisType) {
        return switch (analysisType.toLowerCase()) {
            case "route" -> "Análisis de Ruta";
            case "traceability" -> "Análisis de Trazabilidad";
            case "waste" -> "Análisis de Residuos";
            case "report" -> "Análisis de Reporte";
            case "municipality" -> "Análisis de Municipio";
            case "fleet" -> "Análisis de Flota";
            default -> "Análisis General";
        };
    }

    private List<String> extractRecommendations(String message) {
        List<String> recommendations = new ArrayList<>();
        if (message == null || message.isBlank()) {
            recommendations.add("No se pudieron generar recomendaciones.");
            return recommendations;
        }

        String[] lines = message.split("\n");
        for (String line : lines) {
            String trimmed = line.trim();
            if (trimmed.matches("^\\d+[.)\\-].*") || trimmed.startsWith("- ") || trimmed.startsWith("• ")) {
                String clean = trimmed.replaceFirst("^\\d+[.)\\-]\\s*", "")
                                      .replaceFirst("^[-•]\\s*", "")
                                      .trim();
                if (!clean.isEmpty()) {
                    recommendations.add(clean);
                }
            }
        }

        if (recommendations.isEmpty()) {
            recommendations.add("Revisar los datos proporcionados para un análisis más detallado.");
            recommendations.add("Consultar con el equipo operativo para validar hallazgos.");
        }

        return recommendations;
    }
}

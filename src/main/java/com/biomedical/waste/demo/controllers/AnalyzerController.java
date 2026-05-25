package com.biomedical.waste.demo.controllers;

import com.biomedical.waste.demo.services.AIService;
import com.biomedical.waste.demo.models.ChatRequest;
import com.biomedical.waste.demo.models.ChatResponse;
import com.biomedical.waste.demo.models.AiInteraction;
import com.biomedical.waste.demo.repository.AiInteractionRepository;
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
    private final AiInteractionRepository aiInteractionRepository;

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

        ChatResponse chatResponse;
        if (image != null && !image.isEmpty()) {
            // Use multimodal analysis with image
            chatResponse = aiService.analyzeWithImage(prompt, image);
        } else {
            // Text-only analysis
            ChatRequest chatRequest = new ChatRequest();
            chatRequest.setMessage(prompt);
            chatResponse = aiService.chat(chatRequest);
        }

        Map<String, Object> result = new HashMap<>();
        result.put("title", buildTitle(analysisType));
        result.put("description", chatResponse.getMessage());
        result.put("report", chatResponse.getMessage());
        result.put("recommendations", extractRecommendations(chatResponse.getMessage()));
        result.put("isRelevant", true);
        result.put("relevanceScore", 0.9);
        result.put("relevanceReason", "Análisis generado por IA para: " + analysisType);
        result.put("timestamp", LocalDateTime.now().toString());

        // Save interaction to database
        String imageName = (image != null && !image.isEmpty()) ? image.getOriginalFilename() : null;
        aiInteractionRepository.save(AiInteraction.builder()
            .type("analyzer")
            .userInput(text != null ? text : (imageName != null ? "[Imagen: " + imageName + "]" : ""))
            .aiResponse(chatResponse.getMessage())
            .analysisType(analysisType)
            .imageName(imageName)
            .build());

        return ResponseEntity.ok(result);
    }

    private String buildAnalyzerPrompt(String analysisType, String text, String context, MultipartFile image) {
        StringBuilder prompt = new StringBuilder();
        prompt.append("Eres un analista experto en gestión de residuos biomédicos en Colombia, con conocimiento en normativa (Decreto 351/2014, Resolución 1164/2002). ");
        prompt.append("Tipo de análisis solicitado: ").append(analysisType).append(".\n\n");

        if (context != null && !context.isBlank()) {
            prompt.append("Contexto adicional: ").append(context).append("\n\n");
        }

        if (text != null && !text.isBlank()) {
            prompt.append("Texto a analizar:\n").append(text).append("\n\n");
        }

        if (image != null && !image.isEmpty()) {
            prompt.append("Se adjuntó una imagen para análisis visual. Descríbela en detalle y relacónala con el contexto de residuos biomédicos.\n\n");
        }

        prompt.append("Proporciona un análisis profesional y detallado en español que incluya:\n");
        prompt.append("1. RESUMEN EJECUTIVO: Descripción clara de la situación analizada.\n");
        prompt.append("2. HALLAZGOS PRINCIPALES: Lista detallada de lo encontrado.\n");
        prompt.append("3. EVALUACIÓN DE RIESGO: Nivel de riesgo (bajo/medio/alto/crítico) con justificación.\n");
        prompt.append("4. CUMPLIMIENTO NORMATIVO: Relación con Decreto 351/2014 y Resolución 1164/2002 si aplica.\n");
        prompt.append("5. RECOMENDACIONES: Lista numerada de acciones concretas a tomar.\n");
        prompt.append("6. PRIORIDAD: Indicar urgencia de atención.\n");

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

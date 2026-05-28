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
        result.put("relevanceReason", "AI-generated analysis for: " + analysisType);
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
        prompt.append("You are an expert analyst in biomedical waste management in Colombia, with knowledge of regulations (Decree 351/2014, Resolution 1164/2002). ");
        prompt.append("Requested analysis type: ").append(analysisType).append(".\n\n");

        if (context != null && !context.isBlank()) {
            prompt.append("Additional context: ").append(context).append("\n\n");
        }

        if (text != null && !text.isBlank()) {
            prompt.append("Text to analyze:\n").append(text).append("\n\n");
        }

        if (image != null && !image.isEmpty()) {
            prompt.append("An image was attached for visual analysis. Describe it in detail and relate it to the biomedical waste context.\n\n");
        }

        prompt.append("Provide a professional and detailed analysis in Spanish that includes:\n");
        prompt.append("1. EXECUTIVE SUMMARY: Clear description of the analyzed situation.\n");
        prompt.append("2. KEY FINDINGS: Detailed list of findings.\n");
        prompt.append("3. RISK ASSESSMENT: Risk level (low/medium/high/critical) with justification.\n");
        prompt.append("4. REGULATORY COMPLIANCE: Relation to Decree 351/2014 and Resolution 1164/2002 if applicable.\n");
        prompt.append("5. RECOMMENDATIONS: Numbered list of concrete actions to take.\n");
        prompt.append("6. PRIORITY: Indicate urgency of attention.\n");

        return prompt.toString();
    }

    private String buildTitle(String analysisType) {
        return switch (analysisType.toLowerCase()) {
            case "route" -> "Route Analysis";
            case "traceability" -> "Traceability Analysis";
            case "waste" -> "Waste Analysis";
            case "report" -> "Report Analysis";
            case "municipality" -> "Municipality Analysis";
            case "fleet" -> "Fleet Analysis";
            default -> "General Analysis";
        };
    }

    private List<String> extractRecommendations(String message) {
        List<String> recommendations = new ArrayList<>();
        if (message == null || message.isBlank()) {
            recommendations.add("Could not generate recommendations.");
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
            recommendations.add("Review the provided data for a more detailed analysis.");
            recommendations.add("Consult with the operations team to validate findings.");
        }

        return recommendations;
    }
}

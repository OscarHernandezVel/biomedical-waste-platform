package com.biomedical.waste.demo.controllers;

import com.biomedical.waste.demo.models.ChatRequest;
import com.biomedical.waste.demo.models.ChatResponse;
import com.biomedical.waste.demo.models.AiInteraction;
import com.biomedical.waste.demo.services.AIService;
import com.biomedical.waste.demo.repository.AiInteractionRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/chat")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class ChatController {

    private final AIService aiService;
    private final AiInteractionRepository aiInteractionRepository;

    /** Sends a message to the AI assistant and returns its response. */
    @PostMapping
    public ResponseEntity<?> chat(@RequestBody ChatRequest request) {
        if (request == null || request.getMessage() == null || request.getMessage().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Message cannot be empty");
        }
        ChatResponse response = aiService.chat(request);

        // Save interaction to database
        aiInteractionRepository.save(AiInteraction.builder()
            .type("chat")
            .userInput(request.getMessage())
            .aiResponse(response.getMessage())
            .build());

        if (!response.isSuccess()) {
            String msg = response.getMessage();
            if (response.getError() != null && !response.getError().isBlank()) {
                msg = msg + ": " + response.getError();
            }
            return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body(Map.of("message", msg));
        }
        return ResponseEntity.ok(Map.of("reply", response.getMessage()));
    }

    /** Returns a predefined answer for a supported topic without calling the AI provider. */
    @GetMapping("/quick/{topic}")
    public ResponseEntity<ChatResponse> quickAnswer(@PathVariable String topic) {
        ChatResponse response = aiService.quickAnswer(topic);
        if (response == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(response);
    }

    /** Returns a status payload indicating whether the chat endpoint is online. */
    @GetMapping("/status")
    public ResponseEntity<Map<String, String>> status() {
        return ResponseEntity.ok(Map.of(
            "status", "online",
            "assistant", "Biomedical Waste Assistant",
            "timestamp", LocalDateTime.now().toString()
        ));
    }

    /** Returns the history of all AI interactions saved in the database. */
    @GetMapping("/history")
    public ResponseEntity<List<AiInteraction>> getHistory() {
        return ResponseEntity.ok(aiInteractionRepository.findTop50ByOrderByCreatedAtDesc());
    }
}


package com.biomedical.waste.demo.controllers;

import com.biomedical.waste.demo.models.ChatRequest;
import com.biomedical.waste.demo.models.ChatResponse;
import com.biomedical.waste.demo.services.AIService;
import java.time.LocalDateTime;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/chat")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class ChatController {

    private final AIService aiService;

    /** Sends a message to the AI assistant and returns its response. */
    @PostMapping
    public ResponseEntity<ChatResponse> chat(@RequestBody ChatRequest request) {
        return ResponseEntity.ok(aiService.chat(request));
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
            "assistant", "Asistente de Residuos Biomédicos",
            "timestamp", LocalDateTime.now().toString()
        ));
    }
}


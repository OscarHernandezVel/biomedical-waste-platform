package com.biomedical.waste.demo.models;

import java.util.List;
import lombok.Data;

@Data
public class ChatRequest {
    private String message;
    private List<ChatMessage> history;
}


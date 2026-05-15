package com.biomedical.waste.demo.controllers.dto;

public record AlertDto(
    String id,
    String level,
    String message,
    String createdAt,
    String entityId,
    boolean resolved
) {}


package com.biomedical.waste.demo.models.admin;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum RiskLevel {
    BAJO("Bajo"),
    MEDIO("Medio"),
    ALTO("Alto"),
    CRITICO("Crítico");

    private final String value;

    RiskLevel(String value) {
        this.value = value;
    }

    @JsonValue
    public String value() {
        return value;
    }

    @JsonCreator
    public static RiskLevel from(String raw) {
        if (raw == null) {
            return null;
        }
        String v = raw.trim().toLowerCase();
        return switch (v) {
            case "bajo" -> BAJO;
            case "medio" -> MEDIO;
            case "alto" -> ALTO;
            case "crítico", "critico" -> CRITICO;
            default -> throw new IllegalArgumentException("Nivel de riesgo invalido: " + raw);
        };
    }
}


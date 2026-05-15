package com.biomedical.waste.demo.models.admin;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum Region {
    SUR("Sur"),
    NORTE("Norte"),
    CENTRO("Centro"),
    OCCIDENTE("Occidente"),
    ORIENTE("Oriente");

    private final String value;

    Region(String value) {
        this.value = value;
    }

    @JsonValue
    public String value() {
        return value;
    }

    @JsonCreator
    public static Region from(String raw) {
        if (raw == null) {
            return null;
        }
        String v = raw.trim().toLowerCase();
        return switch (v) {
            case "sur" -> SUR;
            case "norte" -> NORTE;
            case "centro" -> CENTRO;
            case "occidente" -> OCCIDENTE;
            case "oriente" -> ORIENTE;
            default -> throw new IllegalArgumentException("Region invalida: " + raw);
        };
    }
}


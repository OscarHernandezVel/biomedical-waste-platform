package com.biomedical.waste.demo.models.admin;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum WasteLotState {
    GENERADO("generado"),
    EN_RUTA("en ruta"),
    TRATADO("tratado");

    private final String value;

    WasteLotState(String value) {
        this.value = value;
    }

    @JsonValue
    public String value() {
        return value;
    }

    @JsonCreator
    public static WasteLotState from(String raw) {
        if (raw == null) {
            return null;
        }
        String v = raw.trim().toLowerCase();
        return switch (v) {
            case "generado" -> GENERADO;
            case "en ruta", "en_ruta", "enruta" -> EN_RUTA;
            case "tratado" -> TRATADO;
            default -> throw new IllegalArgumentException("Estado de lote invalido: " + raw);
        };
    }
}


package com.biomedical.waste.demo.models.admin;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum FleetState {
    ACTIVO("activo"),
    MANTENIMIENTO("mantenimiento"),
    EN_RUTA("en ruta");

    private final String value;

    FleetState(String value) {
        this.value = value;
    }

    @JsonValue
    public String value() {
        return value;
    }

    @JsonCreator
    public static FleetState from(String raw) {
        if (raw == null) {
            return null;
        }
        String v = raw.trim().toLowerCase();
        return switch (v) {
            case "activo" -> ACTIVO;
            case "mantenimiento" -> MANTENIMIENTO;
            case "en ruta", "en_ruta", "enruta" -> EN_RUTA;
            default -> throw new IllegalArgumentException("Estado invalido: " + raw);
        };
    }
}


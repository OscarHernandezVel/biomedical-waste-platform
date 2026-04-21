package com.biomedical.waste.demo.models;

import lombok.Getter;

@Getter
public enum AlertLevel {
    HIGH("#E24B4A", 1, true),
    MEDIUM("#EF9F27", 2, false),
    LOW("#1D9E75", 3, false);

    private final String color;
    private final int priority;
    private final boolean requiresImmediateAction;

    AlertLevel(String color, int priority, boolean requiresImmediateAction) {
        this.color = color;
        this.priority = priority;
        this.requiresImmediateAction = requiresImmediateAction;
    }
}


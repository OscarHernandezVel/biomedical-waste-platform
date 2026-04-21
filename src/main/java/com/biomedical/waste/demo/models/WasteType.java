package com.biomedical.waste.demo.models;

import lombok.Getter;

@Getter
public enum WasteType {
    INFECTIOUS("Infectious waste", 5, "Autoclaving or incineration"),
    SHARPS("Sharps waste", 4, "Sharps container and incineration"),
    CHEMICAL("Chemical waste", 4, "Neutralization or specialized disposal"),
    PHARMACEUTICAL("Pharmaceutical waste", 3, "Return to manufacturer or incineration"),
    ANATOMICAL("Anatomical waste", 5, "Incineration");

    private final String description;
    private final int riskLevel;
    private final String recommendedTreatment;

    WasteType(String description, int riskLevel, String recommendedTreatment) {
        this.description = description;
        this.riskLevel = riskLevel;
        this.recommendedTreatment = recommendedTreatment;
    }

    /** Returns the matching enum value for the provided string or throws an IllegalArgumentException. */
    public static WasteType fromString(String value) {
        if (value == null) {
            throw new IllegalArgumentException("WasteType value must not be null");
        }
        for (WasteType type : values()) {
            if (type.name().equalsIgnoreCase(value.trim())) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unsupported WasteType value: " + value);
    }
}


package com.biomedical.waste.demo.patterns.factory;

import com.biomedical.waste.demo.models.WasteType;

public class TreatmentFactory {

    /** Creates the correct Treatment subclass based on the waste type. */
    public static Treatment create(WasteType type) {
        return switch (type) {
            case INFECTIOUS -> new AutoclaveTreatment();
            case SHARPS -> new IncinerationTreatment();
            case CHEMICAL -> new ChemicalTreatment();
            case PHARMACEUTICAL -> new PharmaceuticalTreatment();
            case ANATOMICAL -> new IncinerationTreatment();
            default -> throw new IllegalArgumentException("Unknown waste type: " + type);
        };
    }
}

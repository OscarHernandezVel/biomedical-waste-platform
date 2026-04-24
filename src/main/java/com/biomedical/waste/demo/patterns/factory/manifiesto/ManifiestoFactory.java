package com.biomedical.waste.demo.patterns.factory.manifiesto;

import com.biomedical.waste.demo.models.WasteType;

public class ManifiestoFactory {
    public static ManifiestoTransporte create(WasteType type) {
        if (type == null) {
            return new ManifiestoGeneral();
        }
        return switch (type) {
            case SHARPS -> new ManifiestoCortopunzante();
            case ANATOMICAL -> new ManifiestoAnatomopatologico();
            default -> new ManifiestoGeneral();
        };
    }

    public static ManifiestoTransporte create(String tipoResiduo) {
        if (tipoResiduo == null) {
            return new ManifiestoGeneral();
        }
        String t = tipoResiduo.toLowerCase();
        if (t.contains("cortopunz") || t.contains("sharps")) {
            return new ManifiestoCortopunzante();
        }
        if (t.contains("anatomo") || t.contains("anatom")) {
            return new ManifiestoAnatomopatologico();
        }
        return new ManifiestoGeneral();
    }
}

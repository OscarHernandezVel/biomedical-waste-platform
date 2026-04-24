package com.biomedical.waste.demo.patterns.factory.manifiesto;

import com.biomedical.waste.demo.models.OrdenRecoleccion;

public abstract class ManifiestoTransporte {
    protected String documentType;

    public abstract String generate(OrdenRecoleccion orden);

    public String getDocumentType() {
        return documentType;
    }
}


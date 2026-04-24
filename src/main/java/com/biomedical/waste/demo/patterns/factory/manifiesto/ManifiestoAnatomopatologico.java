package com.biomedical.waste.demo.patterns.factory.manifiesto;

import com.biomedical.waste.demo.models.OrdenRecoleccion;

public class ManifiestoAnatomopatologico extends ManifiestoTransporte {

    public ManifiestoAnatomopatologico() {
        this.documentType = "ANATOMICAL_TRANSPORT_MANIFEST";
    }

    @Override
    public String generate(OrdenRecoleccion orden) {
        Long rutaId = orden.getRuta() == null ? null : orden.getRuta().getId();
        return "MANIFIESTO | Tipo: ANATOMOPATOLOGICO | Orden: " + orden.getId()
            + " | Cliente: " + orden.getClienteId()
            + " | Ruta: " + rutaId
            + " | Cantidad: " + orden.getCantidadEstimada();
    }
}

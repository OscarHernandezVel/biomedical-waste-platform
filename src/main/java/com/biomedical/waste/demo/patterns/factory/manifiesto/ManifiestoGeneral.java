package com.biomedical.waste.demo.patterns.factory.manifiesto;

import com.biomedical.waste.demo.models.OrdenRecoleccion;

public class ManifiestoGeneral extends ManifiestoTransporte {

    public ManifiestoGeneral() {
        this.documentType = "GENERAL_TRANSPORT_MANIFEST";
    }

    @Override
    public String generate(OrdenRecoleccion orden) {
        Long rutaId = orden.getRuta() == null ? null : orden.getRuta().getId();
        return "MANIFIESTO | Tipo: GENERAL | Orden: " + orden.getId()
            + " | Cliente: " + orden.getClienteId()
            + " | Ruta: " + rutaId
            + " | Cantidad: " + orden.getCantidadEstimada();
    }
}

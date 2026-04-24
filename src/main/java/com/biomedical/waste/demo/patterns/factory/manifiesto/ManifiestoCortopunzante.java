package com.biomedical.waste.demo.patterns.factory.manifiesto;

import com.biomedical.waste.demo.models.OrdenRecoleccion;

public class ManifiestoCortopunzante extends ManifiestoTransporte {

    public ManifiestoCortopunzante() {
        this.documentType = "SHARPS_TRANSPORT_MANIFEST";
    }

    @Override
    public String generate(OrdenRecoleccion orden) {
        Long rutaId = orden.getRuta() == null ? null : orden.getRuta().getId();
        return "MANIFIESTO | Tipo: CORTOPUNZANTE | Orden: " + orden.getId()
            + " | Cliente: " + orden.getClienteId()
            + " | Ruta: " + rutaId
            + " | Cantidad: " + orden.getCantidadEstimada();
    }
}

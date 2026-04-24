package com.biomedical.waste.demo.services.ordenes;

import com.biomedical.waste.demo.models.OrdenRecoleccion;
import com.biomedical.waste.demo.models.OrdenRecoleccionStatus;

public interface OrdenRecoleccionObserver {
    void onStatusChanged(OrdenRecoleccion orden, OrdenRecoleccionStatus previous, OrdenRecoleccionStatus current);
}


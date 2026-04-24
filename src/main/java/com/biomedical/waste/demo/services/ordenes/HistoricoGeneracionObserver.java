package com.biomedical.waste.demo.services.ordenes;

import com.biomedical.waste.demo.models.OrdenRecoleccion;
import com.biomedical.waste.demo.models.OrdenRecoleccionStatus;
import com.biomedical.waste.demo.services.prediccion.PrediccionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class HistoricoGeneracionObserver implements OrdenRecoleccionObserver {

    private final PrediccionService prediccionService;

    @Override
    public void onStatusChanged(OrdenRecoleccion orden, OrdenRecoleccionStatus previous, OrdenRecoleccionStatus current) {
        if (current == OrdenRecoleccionStatus.COMPLETED) {
            prediccionService.recordCompletedOrder(orden);
        }
    }
}

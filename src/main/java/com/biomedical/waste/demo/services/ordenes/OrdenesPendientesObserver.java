package com.biomedical.waste.demo.services.ordenes;

import com.biomedical.waste.demo.models.OrdenRecoleccion;
import com.biomedical.waste.demo.models.OrdenRecoleccionStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrdenesPendientesObserver implements OrdenRecoleccionObserver {

    private final OrdenesPendientesService ordenesPendientesService;

    @Override
    public void onStatusChanged(OrdenRecoleccion orden, OrdenRecoleccionStatus previous, OrdenRecoleccionStatus current) {
        ordenesPendientesService.onStatusChanged(orden, current);
    }
}

package com.biomedical.waste.demo.services.ordenes;

import com.biomedical.waste.demo.models.OrdenRecoleccion;
import com.biomedical.waste.demo.models.OrdenRecoleccionStatus;
import com.biomedical.waste.demo.services.manifiestos.ManifiestoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ManifiestoObserver implements OrdenRecoleccionObserver {

    private final ManifiestoService manifiestoService;

    @Override
    public void onStatusChanged(OrdenRecoleccion orden, OrdenRecoleccionStatus previous, OrdenRecoleccionStatus current) {
        if (current == OrdenRecoleccionStatus.COMPLETED) {
            manifiestoService.generar(orden);
        }
    }
}


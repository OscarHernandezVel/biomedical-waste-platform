package com.biomedical.waste.demo.services.ordenes;

import com.biomedical.waste.demo.models.OrdenRecoleccion;
import com.biomedical.waste.demo.models.OrdenRecoleccionStatus;
import com.biomedical.waste.demo.models.Ruta;
import com.biomedical.waste.demo.repository.OrdenRecoleccionRepository;
import com.biomedical.waste.demo.repository.RutaDbRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrdenRecoleccionService {

    private final List<OrdenRecoleccionObserver> observers;
    private final OrdenRecoleccionRepository ordenRecoleccionRepository;
    private final RutaDbRepository rutaDbRepository;

    public OrdenRecoleccion create(Long clienteId, Long rutaId, OrdenRecoleccion orden) {
        if (orden == null) {
            throw new IllegalArgumentException("OrdenRecoleccion cannot be null");
        }
        Ruta ruta = null;
        if (rutaId != null) {
            ruta = rutaDbRepository.findById(rutaId).orElse(null);
        }
        OrdenRecoleccion created = OrdenRecoleccion.builder()
            .codigoOrden(orden.getCodigoOrden())
            .clienteId(clienteId)
            .ruta(ruta)
            .fechaProgramada(orden.getFechaProgramada())
            .fechaEjecutada(orden.getFechaEjecutada())
            .status(OrdenRecoleccionStatus.PENDING)
            .tipoResiduo(orden.getTipoResiduo())
            .cantidadEstimada(orden.getCantidadEstimada())
            .build();
        return ordenRecoleccionRepository.save(created);
    }

    public OrdenRecoleccion updateStatus(Long ordenId, OrdenRecoleccionStatus newStatus) {
        if (newStatus == null) {
            throw new IllegalArgumentException("OrdenRecoleccionStatus cannot be null");
        }
        OrdenRecoleccion orden = ordenRecoleccionRepository.findById(ordenId)
            .orElseThrow(() -> new IllegalArgumentException("OrdenRecoleccion not found: " + ordenId));
        OrdenRecoleccionStatus previous = orden.getStatus();
        orden.setStatus(newStatus);
        if (newStatus == OrdenRecoleccionStatus.COMPLETED && orden.getFechaEjecutada() == null) {
            orden.setFechaEjecutada(java.time.LocalDateTime.now());
        }
        OrdenRecoleccion saved = ordenRecoleccionRepository.save(orden);
        notifyObservers(saved, previous, newStatus);
        return saved;
    }

    private void notifyObservers(OrdenRecoleccion orden, OrdenRecoleccionStatus previous, OrdenRecoleccionStatus current) {
        for (OrdenRecoleccionObserver observer : observers) {
            observer.onStatusChanged(orden, previous, current);
        }
    }
}

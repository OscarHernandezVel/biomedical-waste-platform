package com.biomedical.waste.demo.services.ordenes;

import com.biomedical.waste.demo.models.OrdenRecoleccion;
import com.biomedical.waste.demo.models.OrdenRecoleccionStatus;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Service;

@Service
public class OrdenesPendientesService {

    private final Map<Long, OrdenRecoleccionStatus> index = new LinkedHashMap<>();

    public void onStatusChanged(OrdenRecoleccion orden, OrdenRecoleccionStatus status) {
        if (orden == null || orden.getId() == null) {
            return;
        }
        if (status == OrdenRecoleccionStatus.COMPLETED || status == OrdenRecoleccionStatus.CANCELLED) {
            index.remove(orden.getId());
            return;
        }
        index.put(orden.getId(), status);
    }

    public List<String> getPendingOrderIds() {
        return index.keySet().stream().map(String::valueOf).toList();
    }
}

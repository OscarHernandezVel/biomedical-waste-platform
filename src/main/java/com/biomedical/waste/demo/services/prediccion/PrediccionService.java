package com.biomedical.waste.demo.services.prediccion;

import com.biomedical.waste.demo.models.OrdenRecoleccion;
import com.biomedical.waste.demo.models.Waste;
import com.biomedical.waste.demo.models.WasteType;
import com.biomedical.waste.demo.repository.WasteRepository;
import java.util.EnumMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PrediccionService {

    private final WasteRepository wasteRepository;

    public void recordCompletedOrder(OrdenRecoleccion orden) {
        if (orden == null) {
            throw new IllegalArgumentException("OrdenRecoleccion cannot be null");
        }
    }

    public Map<WasteType, Long> baselineCountByType() {
        Map<WasteType, Long> result = new EnumMap<>(WasteType.class);
        for (WasteType type : WasteType.values()) {
            result.put(type, 0L);
        }
        for (Waste waste : wasteRepository.findAll()) {
            WasteType type = waste.getType();
            if (type != null) {
                result.put(type, result.get(type) + 1);
            }
        }
        return result;
    }
}

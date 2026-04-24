package com.biomedical.waste.demo.services.tarifas;

import com.biomedical.waste.demo.models.WasteType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TarifaContext {
    private String clienteId;
    private String tipoInstalacion;
    private WasteType wasteType;
    private Double weightKg;
}


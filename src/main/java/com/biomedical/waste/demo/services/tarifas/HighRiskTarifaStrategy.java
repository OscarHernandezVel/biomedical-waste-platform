package com.biomedical.waste.demo.services.tarifas;

import com.biomedical.waste.demo.models.WasteType;
import org.springframework.stereotype.Service;

@Service
public class HighRiskTarifaStrategy implements TarifaStrategy {

    @Override
    public double calculate(TarifaContext context) {
        double kg = context.getWeightKg() == null ? 0.0 : context.getWeightKg();
        double base = 4500.0 * kg;
        return base * 1.35;
    }

    @Override
    public boolean supports(TarifaContext context) {
        return context != null && (context.getWasteType() == WasteType.INFECTIOUS || context.getWasteType() == WasteType.ANATOMICAL);
    }

    @Override
    public String name() {
        return "HIGH_RISK";
    }

    @Override
    public int priority() {
        return 10;
    }
}

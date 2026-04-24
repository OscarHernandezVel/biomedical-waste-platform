package com.biomedical.waste.demo.services.tarifas;

import org.springframework.stereotype.Service;

@Service
public class StandardTarifaStrategy implements TarifaStrategy {

    @Override
    public double calculate(TarifaContext context) {
        double kg = context.getWeightKg() == null ? 0.0 : context.getWeightKg();
        return 4500.0 * kg;
    }

    @Override
    public boolean supports(TarifaContext context) {
        return true;
    }

    @Override
    public String name() {
        return "STANDARD";
    }

    @Override
    public int priority() {
        return 100;
    }
}

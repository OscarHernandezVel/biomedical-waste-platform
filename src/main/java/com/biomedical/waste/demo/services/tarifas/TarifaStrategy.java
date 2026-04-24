package com.biomedical.waste.demo.services.tarifas;

public interface TarifaStrategy {
    double calculate(TarifaContext context);
    boolean supports(TarifaContext context);
    String name();
    int priority();
}

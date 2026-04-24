package com.biomedical.waste.demo.services.tarifas;

import java.util.Comparator;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TarifaService {

    private final List<TarifaStrategy> strategies;

    public TarifaQuote quote(TarifaContext context) {
        if (context == null) {
            throw new IllegalArgumentException("TarifaContext cannot be null");
        }
        TarifaStrategy selected = strategies.stream()
            .filter(s -> s.supports(context))
            .min(Comparator.comparingInt(TarifaStrategy::priority))
            .orElseThrow(() -> new IllegalStateException("No TarifaStrategy available"));
        double amount = selected.calculate(context);
        return new TarifaQuote(selected.name(), amount);
    }

    public record TarifaQuote(String strategy, double amount) {}
}

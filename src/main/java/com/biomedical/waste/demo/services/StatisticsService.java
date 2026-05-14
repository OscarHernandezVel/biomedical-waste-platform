package com.biomedical.waste.demo.services;

import com.biomedical.waste.demo.models.AlertLevel;
import com.biomedical.waste.demo.models.Waste;
import com.biomedical.waste.demo.models.WasteType;
import com.biomedical.waste.demo.repository.AlertRepository;
import com.biomedical.waste.demo.repository.WasteRepository;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StatisticsService {

    private final WasteRepository wasteRepository;
    private final AlertRepository alertRepository;

    /** Returns the count of waste records grouped by type. */
    public Map<WasteType, Long> countByType() {
        return wasteRepository.findAll().stream()
            .collect(Collectors.groupingBy(Waste::getType, Collectors.counting()));
    }

    /** Returns total weight in kg grouped by originating entity. */
    public Map<String, Double> totalWeightByEntity() {
        return wasteRepository.findAll().stream()
            .collect(Collectors.groupingBy(Waste::getOriginEntity, Collectors.summingDouble(Waste::getWeightKg)));
    }

    /** Returns alert count grouped by severity level. */
    public Map<AlertLevel, Long> countAlertsByLevel() {
        Map<AlertLevel, Long> result = new java.util.LinkedHashMap<>();
        for (AlertLevel level : AlertLevel.values()) {
            result.put(level, alertRepository.countByLevel(level));
        }
        return result;
    }

    /** Returns the N most recently registered waste items. */
    public List<Waste> getMostRecent(int count) {
        return wasteRepository.findTop10ByOrderByGenerationDateDesc()
            .stream()
            .limit(count)
            .collect(Collectors.toList());
    }

    /** Returns the average weight per waste collection, or 0.0 if no records exist. */
    public Double getAverageWeight() {
        return wasteRepository.findAll().stream()
            .mapToDouble(Waste::getWeightKg)
            .average()
            .orElse(0.0);
    }

    /** Returns the waste type that appears most frequently in the database. */
    public WasteType getMostFrequentType() {
        return countByType().entrySet().stream()
            .max(Map.Entry.comparingByValue())
            .map(Map.Entry::getKey)
            .orElse(null);
    }

    public Map<String, Object> summary() {
        long totalWastes = wasteRepository.count();
        long activeAlerts = alertRepository.findByResolved(false).size();
        Map<WasteType, Long> byType = countByType();
        WasteType mostFrequent = byType.entrySet().stream()
            .max(Map.Entry.comparingByValue())
            .map(Map.Entry::getKey)
            .orElse(null);

        double totalWeight = wasteRepository.findAll().stream().mapToDouble(Waste::getWeightKg).sum();

        Map<String, Object> out = new java.util.LinkedHashMap<>();
        out.put("totalWastes", totalWastes);
        out.put("activeAlerts", activeAlerts);
        out.put("totalWeightKg", totalWeight);
        out.put("mostFrequentType", mostFrequent == null ? null : mostFrequent.name());
        out.put("distribution", byType.entrySet().stream().collect(Collectors.toMap(e -> e.getKey().name(), Map.Entry::getValue)));
        return out;
    }

    public List<Map<String, Object>> trend(int days) {
        int window = Math.max(1, Math.min(days, 90));
        LocalDate end = LocalDate.now();
        LocalDate start = end.minusDays(window - 1L);

        Map<LocalDate, Long> counts = wasteRepository.findAll().stream()
            .filter(w -> w.getGenerationDate() != null)
            .map(w -> w.getGenerationDate().toLocalDate())
            .filter(d -> !d.isBefore(start) && !d.isAfter(end))
            .collect(Collectors.groupingBy(d -> d, TreeMap::new, Collectors.counting()));

        for (int i = 0; i < window; i++) {
            LocalDate d = start.plusDays(i);
            counts.putIfAbsent(d, 0L);
        }

        return counts.entrySet().stream()
            .map(e -> {
                Map<String, Object> row = new java.util.LinkedHashMap<>();
                row.put("date", e.getKey().toString());
                row.put("count", e.getValue());
                return row;
            })
            .toList();
    }

    public Map<String, Long> distribution() {
        return countByType().entrySet().stream()
            .collect(Collectors.toMap(e -> e.getKey().name(), Map.Entry::getValue));
    }

    public List<Map<String, Object>> topGenerators(int limit) {
        int n = Math.max(1, Math.min(limit, 20));
        return totalWeightByEntity().entrySet().stream()
            .sorted((a, b) -> Double.compare(b.getValue(), a.getValue()))
            .limit(n)
            .map(e -> {
                Map<String, Object> row = new java.util.LinkedHashMap<>();
                row.put("entity", e.getKey());
                row.put("totalWeightKg", e.getValue());
                return row;
            })
            .toList();
    }
}


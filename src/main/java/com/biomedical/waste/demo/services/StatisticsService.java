package com.biomedical.waste.demo.services;

import com.biomedical.waste.demo.models.AlertLevel;
import com.biomedical.waste.demo.models.Waste;
import com.biomedical.waste.demo.models.WasteType;
import com.biomedical.waste.demo.repository.AlertRepository;
import com.biomedical.waste.demo.repository.WasteRepository;
import java.util.List;
import java.util.Map;
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
}


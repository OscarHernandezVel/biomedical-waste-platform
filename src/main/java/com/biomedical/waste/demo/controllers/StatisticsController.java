package com.biomedical.waste.demo.controllers;

import com.biomedical.waste.demo.models.AlertLevel;
import com.biomedical.waste.demo.models.Waste;
import com.biomedical.waste.demo.models.WasteType;
import com.biomedical.waste.demo.services.StatisticsService;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/statistics")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class StatisticsController {

    private final StatisticsService statisticsService;

    /** Returns the count of waste records grouped by waste type. */
    @GetMapping("/by-type")
    public ResponseEntity<Map<WasteType, Long>> countByType() {
        return ResponseEntity.ok(statisticsService.countByType());
    }

    /** Returns the total weight in kg grouped by originating entity. */
    @GetMapping("/weight-by-entity")
    public ResponseEntity<Map<String, Double>> totalWeightByEntity() {
        return ResponseEntity.ok(statisticsService.totalWeightByEntity());
    }

    /** Returns the alert count grouped by severity level. */
    @GetMapping("/alerts-by-level")
    public ResponseEntity<Map<AlertLevel, Long>> countAlertsByLevel() {
        return ResponseEntity.ok(statisticsService.countAlertsByLevel());
    }

    /** Returns the N most recently registered waste items. */
    @GetMapping("/most-recent/{count}")
    public ResponseEntity<List<Waste>> getMostRecent(@PathVariable int count) {
        return ResponseEntity.ok(statisticsService.getMostRecent(count));
    }

    /** Returns the average weight per waste collection in kg. */
    @GetMapping("/average-weight")
    public ResponseEntity<Double> getAverageWeight() {
        return ResponseEntity.ok(statisticsService.getAverageWeight());
    }

    /** Returns the waste type that appears most frequently in the database. */
    @GetMapping("/most-frequent-type")
    public ResponseEntity<WasteType> getMostFrequentType() {
        return ResponseEntity.ok(statisticsService.getMostFrequentType());
    }
}


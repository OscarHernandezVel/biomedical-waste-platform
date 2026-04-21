package com.biomedical.waste.demo.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

import com.biomedical.waste.demo.models.AlertLevel;
import com.biomedical.waste.demo.models.Waste;
import com.biomedical.waste.demo.models.WasteType;
import com.biomedical.waste.demo.services.StatisticsService;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

class StatisticsControllerTest {

    @Test
    void countByTypeReturnsOk() {
        StatisticsService statisticsService = Mockito.mock(StatisticsService.class);
        StatisticsController controller = new StatisticsController(statisticsService);
        when(statisticsService.countByType()).thenReturn(Map.of(WasteType.INFECTIOUS, 1L));
        ResponseEntity<Map<WasteType, Long>> response = controller.countByType();
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    void totalWeightByEntityReturnsOk() {
        StatisticsService statisticsService = Mockito.mock(StatisticsService.class);
        StatisticsController controller = new StatisticsController(statisticsService);
        when(statisticsService.totalWeightByEntity()).thenReturn(Map.of("Hospital", 12.5));
        ResponseEntity<Map<String, Double>> response = controller.totalWeightByEntity();
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    void countAlertsByLevelReturnsOk() {
        StatisticsService statisticsService = Mockito.mock(StatisticsService.class);
        StatisticsController controller = new StatisticsController(statisticsService);
        Map<AlertLevel, Long> result = new LinkedHashMap<>();
        result.put(AlertLevel.HIGH, 2L);
        result.put(AlertLevel.MEDIUM, 1L);
        result.put(AlertLevel.LOW, 0L);
        when(statisticsService.countAlertsByLevel()).thenReturn(result);
        ResponseEntity<Map<AlertLevel, Long>> response = controller.countAlertsByLevel();
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    void getMostRecentReturnsOk() {
        StatisticsService statisticsService = Mockito.mock(StatisticsService.class);
        StatisticsController controller = new StatisticsController(statisticsService);
        when(statisticsService.getMostRecent(3)).thenReturn(List.of(sampleWaste("w1")));
        ResponseEntity<List<Waste>> response = controller.getMostRecent(3);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    void getAverageWeightReturnsOk() {
        StatisticsService statisticsService = Mockito.mock(StatisticsService.class);
        StatisticsController controller = new StatisticsController(statisticsService);
        when(statisticsService.getAverageWeight()).thenReturn(3.5);
        ResponseEntity<Double> response = controller.getAverageWeight();
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(3.5, response.getBody());
    }

    @Test
    void getMostFrequentTypeReturnsOk() {
        StatisticsService statisticsService = Mockito.mock(StatisticsService.class);
        StatisticsController controller = new StatisticsController(statisticsService);
        when(statisticsService.getMostFrequentType()).thenReturn(WasteType.INFECTIOUS);
        ResponseEntity<WasteType> response = controller.getMostFrequentType();
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(WasteType.INFECTIOUS, response.getBody());
    }

    private Waste sampleWaste(String id) {
        return Waste.builder()
            .id(id)
            .type(WasteType.INFECTIOUS)
            .weightKg(1.0)
            .originEntity("Hospital")
            .generationDate(LocalDateTime.now())
            .build();
    }
}

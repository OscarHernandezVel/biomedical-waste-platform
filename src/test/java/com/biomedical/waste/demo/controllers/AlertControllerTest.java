package com.biomedical.waste.demo.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.biomedical.waste.demo.models.Alert;
import com.biomedical.waste.demo.models.AlertLevel;
import com.biomedical.waste.demo.models.Waste;
import com.biomedical.waste.demo.models.WasteType;
import com.biomedical.waste.demo.services.AlertService;
import com.biomedical.waste.demo.services.WasteService;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

class AlertControllerTest {

    @Test
    void getActiveReturnsOk() {
        AlertService alertService = Mockito.mock(AlertService.class);
        WasteService wasteService = Mockito.mock(WasteService.class);
        AlertController controller = new AlertController(alertService, wasteService);
        when(alertService.getActive()).thenReturn(List.of());
        ResponseEntity<List<Alert>> response = controller.getActive();
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void getHistoryReturnsOk() {
        AlertService alertService = Mockito.mock(AlertService.class);
        WasteService wasteService = Mockito.mock(WasteService.class);
        AlertController controller = new AlertController(alertService, wasteService);
        when(alertService.getHistory()).thenReturn(List.of());
        ResponseEntity<List<Alert>> response = controller.getHistory();
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void generateAlertReturnsOk() {
        AlertService alertService = Mockito.mock(AlertService.class);
        WasteService wasteService = Mockito.mock(WasteService.class);
        AlertController controller = new AlertController(alertService, wasteService);
        Waste waste = Waste.builder()
            .id("w1")
            .type(WasteType.INFECTIOUS)
            .weightKg(1.0)
            .originEntity("Hospital San Rafael")
            .generationDate(LocalDateTime.now())
            .build();
        when(wasteService.getById("w1")).thenReturn(waste);
        when(alertService.generateAlert(any(Waste.class))).thenReturn(sampleAlert("a1"));
        ResponseEntity<Alert> response = controller.generateAlert("w1");
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        verify(alertService).generateAlert(waste);
    }

    @Test
    void resolveLatestReturnsOk() {
        AlertService alertService = Mockito.mock(AlertService.class);
        WasteService wasteService = Mockito.mock(WasteService.class);
        AlertController controller = new AlertController(alertService, wasteService);
        when(alertService.resolveLatest()).thenReturn(sampleAlert("a2"));
        ResponseEntity<Alert> response = controller.resolveLatest();
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    void countByLevelReturnsOk() {
        AlertService alertService = Mockito.mock(AlertService.class);
        WasteService wasteService = Mockito.mock(WasteService.class);
        AlertController controller = new AlertController(alertService, wasteService);
        when(alertService.countByLevel(AlertLevel.HIGH)).thenReturn(3L);
        ResponseEntity<Long> response = controller.countByLevel(AlertLevel.HIGH);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(3L, response.getBody());
    }

    private Alert sampleAlert(String id) {
        return Alert.builder()
            .id(id)
            .message("Test alert")
            .level(AlertLevel.HIGH)
            .date(LocalDateTime.now())
            .resolved(false)
            .wasteId("w1")
            .build();
    }
}

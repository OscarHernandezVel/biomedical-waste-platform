package com.biomedical.waste.demo.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.biomedical.waste.demo.models.Waste;
import com.biomedical.waste.demo.models.WasteStatus;
import com.biomedical.waste.demo.models.WasteType;
import com.biomedical.waste.demo.services.WasteService;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

class WasteControllerTest {

    @Test
    void getAllReturnsOk() {
        WasteService wasteService = Mockito.mock(WasteService.class);
        WasteController controller = new WasteController(wasteService);
        when(wasteService.getAll()).thenReturn(List.of());
        ResponseEntity<List<Waste>> response = controller.getAll();
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    void getByIdReturnsOk() {
        WasteService wasteService = Mockito.mock(WasteService.class);
        WasteController controller = new WasteController(wasteService);
        when(wasteService.getById("w1")).thenReturn(sampleWaste("w1"));
        ResponseEntity<Waste> response = controller.getById("w1");
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    void createReturnsOk() {
        WasteService wasteService = Mockito.mock(WasteService.class);
        WasteController controller = new WasteController(wasteService);
        when(wasteService.create(Mockito.any(Waste.class))).thenReturn(sampleWaste("w2"));
        Waste request = sampleWaste(null);
        ResponseEntity<Waste> response = controller.create(request);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    void updateReturnsOk() {
        WasteService wasteService = Mockito.mock(WasteService.class);
        WasteController controller = new WasteController(wasteService);
        when(wasteService.update(Mockito.any(String.class), Mockito.any(Waste.class))).thenReturn(sampleWaste("w3"));
        ResponseEntity<Waste> response = controller.update("w3", sampleWaste(null));
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    void deleteReturnsNoContent() {
        WasteService wasteService = Mockito.mock(WasteService.class);
        WasteController controller = new WasteController(wasteService);
        ResponseEntity<Void> response = controller.delete("w4");
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(wasteService).delete("w4");
    }

    @Test
    void getByTypeReturnsOk() {
        WasteService wasteService = Mockito.mock(WasteService.class);
        WasteController controller = new WasteController(wasteService);
        when(wasteService.getByType(WasteType.INFECTIOUS)).thenReturn(List.of());
        ResponseEntity<List<Waste>> response = controller.getByType(WasteType.INFECTIOUS);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void getByEntityReturnsOk() {
        WasteService wasteService = Mockito.mock(WasteService.class);
        WasteController controller = new WasteController(wasteService);
        when(wasteService.getByEntity("Hospital San Rafael")).thenReturn(List.of());
        ResponseEntity<List<Waste>> response = controller.getByEntity("Hospital San Rafael");
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void getTotalWeightReturnsOk() {
        WasteService wasteService = Mockito.mock(WasteService.class);
        WasteController controller = new WasteController(wasteService);
        when(wasteService.getTotalWeight()).thenReturn(12.5);
        ResponseEntity<Double> response = controller.getTotalWeight();
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(12.5, response.getBody());
    }

    @Test
    void processWasteReturnsOk() {
        WasteService wasteService = Mockito.mock(WasteService.class);
        WasteController controller = new WasteController(wasteService);
        when(wasteService.processWaste("w5")).thenReturn(sampleWaste("w5"));
        ResponseEntity<Waste> response = controller.processWaste("w5");
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    void cloneWasteReturnsOk() {
        WasteService wasteService = Mockito.mock(WasteService.class);
        WasteController controller = new WasteController(wasteService);
        when(wasteService.cloneWaste("w6")).thenReturn(sampleWaste("w6"));
        ResponseEntity<Waste> response = controller.cloneWaste("w6");
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    void advanceStatusReturnsOk() {
        WasteService wasteService = Mockito.mock(WasteService.class);
        WasteController controller = new WasteController(wasteService);
        when(wasteService.advanceStatus("w7")).thenReturn(sampleWaste("w7"));
        ResponseEntity<Waste> response = controller.advanceStatus("w7");
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    void getTraceLogReturnsOk() {
        WasteService wasteService = Mockito.mock(WasteService.class);
        WasteController controller = new WasteController(wasteService);
        when(wasteService.getTraceLog()).thenReturn(List.of());
        ResponseEntity<List<String>> response = controller.getTraceLog();
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void getWeightsSortedReturnsOk() {
        WasteService wasteService = Mockito.mock(WasteService.class);
        WasteController controller = new WasteController(wasteService);
        when(wasteService.getWeightsSorted()).thenReturn(List.of(1.0, 2.0));
        ResponseEntity<List<Double>> response = controller.getWeightsSorted();
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    private Waste sampleWaste(String id) {
        return Waste.builder()
            .id(id)
            .type(WasteType.INFECTIOUS)
            .weightKg(1.0)
            .originEntity("Hospital San Rafael")
            .generationDate(LocalDateTime.now())
            .status(WasteStatus.PENDING)
            .build();
    }
}


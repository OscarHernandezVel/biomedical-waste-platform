package com.biomedical.waste.demo.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

import com.biomedical.waste.demo.models.Route;
import com.biomedical.waste.demo.services.RouteService;
import com.biomedical.waste.demo.structures.RouteGraph;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

class RouteControllerTest {

    @Test
    void getAllRoutesReturnsOk() {
        RouteService routeService = Mockito.mock(RouteService.class);
        RouteController controller = new RouteController(routeService);
        when(routeService.getAllRoutes()).thenReturn(List.of());
        ResponseEntity<List<Route>> response = controller.getAllRoutes();
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void createRouteReturnsOk() {
        RouteService routeService = Mockito.mock(RouteService.class);
        RouteController controller = new RouteController(routeService);
        when(routeService.createRoute(Mockito.any(Route.class))).thenReturn(sampleRoute("r1"));
        ResponseEntity<Route> response = controller.createRoute(sampleRoute(null));
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    void getOptimalRouteReturnsOk() {
        RouteService routeService = Mockito.mock(RouteService.class);
        RouteController controller = new RouteController(routeService);
        RouteGraph.PathResult result = new RouteGraph.PathResult(List.of("A", "B"), 1.0, 4);
        when(routeService.getOptimalRoute("A", "B")).thenReturn(result);
        ResponseEntity<RouteGraph.PathResult> response = controller.getOptimalRoute("A", "B");
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    void getReachablePointsReturnsOk() {
        RouteService routeService = Mockito.mock(RouteService.class);
        RouteController controller = new RouteController(routeService);
        when(routeService.getReachablePoints("A")).thenReturn(List.of("A", "B"));
        ResponseEntity<List<String>> response = controller.getReachablePoints("A");
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void addCollectionPointReturnsOk() {
        RouteService routeService = Mockito.mock(RouteService.class);
        RouteController controller = new RouteController(routeService);
        ResponseEntity<String> response = controller.addCollectionPoint("X", Map.of("Y", 2.0));
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    private Route sampleRoute(String id) {
        return Route.builder()
            .id(id)
            .name("Route1")
            .status("PENDING")
            .date(LocalDate.now())
            .distanceKm(3.2)
            .assignedDriver("Driver1")
            .build();
    }
}

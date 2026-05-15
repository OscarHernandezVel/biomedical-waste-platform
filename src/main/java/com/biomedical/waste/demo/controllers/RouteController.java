package com.biomedical.waste.demo.controllers;

import com.biomedical.waste.demo.models.Route;
import com.biomedical.waste.demo.models.RouteStop;
import com.biomedical.waste.demo.services.RouteService;
import com.biomedical.waste.demo.structures.RouteGraph;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/routes")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class RouteController {

    private final RouteService routeService;

    /** Returns all route records from the database. */
    @GetMapping
    public ResponseEntity<List<Route>> getAllRoutes() {
        return ResponseEntity.ok(routeService.getAllRoutes());
    }

    /** Creates and saves a new route record. */
    @PostMapping
    public ResponseEntity<Route> createRoute(@RequestBody Route route) {
        return ResponseEntity.status(HttpStatus.CREATED).body(routeService.createRoute(route));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Route> getById(@PathVariable String id) {
        return ResponseEntity.ok(routeService.getById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Route> update(@PathVariable String id, @RequestBody Route payload) {
        return ResponseEntity.ok(routeService.updateRoute(id, payload));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        routeService.deleteRoute(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/stops")
    public ResponseEntity<List<RouteStop>> listStops(@PathVariable String id) {
        return ResponseEntity.ok(routeService.listStops(id));
    }

    @PostMapping("/{id}/stops")
    public ResponseEntity<RouteStop> createStop(@PathVariable String id, @RequestBody RouteStop stop) {
        return ResponseEntity.status(HttpStatus.CREATED).body(routeService.createStop(id, stop));
    }

    @PutMapping("/{id}/stops/{stopId}")
    public ResponseEntity<RouteStop> updateStop(@PathVariable String id, @PathVariable String stopId, @RequestBody RouteStop payload) {
        return ResponseEntity.ok(routeService.updateStop(id, stopId, payload));
    }

    @DeleteMapping("/{id}/stops/{stopId}")
    public ResponseEntity<Void> deleteStop(@PathVariable String id, @PathVariable String stopId) {
        routeService.deleteStop(id, stopId);
        return ResponseEntity.noContent().build();
    }

    /** Returns the optimal route between two collection points using Dijkstra. */
    @GetMapping("/optimal")
    public ResponseEntity<RouteGraph.PathResult> getOptimalRoute(@RequestParam String from, @RequestParam String to) {
        return ResponseEntity.ok(routeService.getOptimalRoute(from, to));
    }

    /** Returns all collection points reachable from the given origin using BFS. */
    @GetMapping("/reachable")
    public ResponseEntity<List<String>> getReachablePoints(@RequestParam String origin) {
        return ResponseEntity.ok(routeService.getReachablePoints(origin));
    }

    /** Adds a new collection point with its route connections to the graph. */
    @PostMapping("/collection-point")
    public ResponseEntity<String> addCollectionPoint(@RequestParam String name, @RequestBody Map<String, Double> connections) {
        routeService.addCollectionPoint(name, connections);
        return ResponseEntity.ok("Collection point '" + name + "' added successfully.");
    }
}


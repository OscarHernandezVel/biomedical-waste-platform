package com.biomedical.waste.demo.controllers;

import com.biomedical.waste.demo.models.Route;
import com.biomedical.waste.demo.services.RouteService;
import com.biomedical.waste.demo.structures.RouteGraph;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
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
        return ResponseEntity.ok(routeService.createRoute(route));
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


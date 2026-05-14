package com.biomedical.waste.demo.services;

import com.biomedical.waste.demo.models.Route;
import com.biomedical.waste.demo.repository.RouteRepository;
import com.biomedical.waste.demo.structures.RouteGraph;
import jakarta.annotation.PostConstruct;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class RouteService {

    private final RouteRepository routeRepository;
    private final RouteGraph routeGraph = new RouteGraph();

    /** Initializes the in-memory route graph with default biomedical collection points. */
    @PostConstruct
    public void initializeGraph() {
        routeGraph.addNode("HospitalSanRafael");
        routeGraph.addNode("ClinicaSur");
        routeGraph.addNode("LaboratorioCentral");
        routeGraph.addNode("CentroVeterinario");
        routeGraph.addNode("HospitalPediatrico");
        routeGraph.addNode("DepositoCentral");
        routeGraph.addRoute("HospitalSanRafael", "ClinicaSur", 3.2, 12);
        routeGraph.addRoute("HospitalSanRafael", "LaboratorioCentral", 5.1, 18);
        routeGraph.addRoute("ClinicaSur", "DepositoCentral", 2.8, 10);
        routeGraph.addRoute("LaboratorioCentral", "CentroVeterinario", 4.0, 15);
        routeGraph.addRoute("CentroVeterinario", "HospitalPediatrico", 3.5, 13);
        routeGraph.addRoute("HospitalPediatrico", "DepositoCentral", 6.2, 22);
    }

    /** Calculates the shortest collection route between two points using Dijkstra. */
    public RouteGraph.PathResult getOptimalRoute(String from, String to) {
        return routeGraph.dijkstra(from, to);
    }

    /** Returns all collection points reachable from the given origin using BFS. */
    public List<String> getReachablePoints(String origin) {
        return routeGraph.bfs(origin);
    }

    /** Adds a new collection point with its connections to the route network. */
    public void addCollectionPoint(String name, Map<String, Double> connections) {
        routeGraph.addNode(name);
        connections.forEach((dest, dist) -> routeGraph.addRoute(name, dest, dist, (int) (dist * 4)));
    }

    /** Returns all route records from the database. */
    public List<Route> getAllRoutes() {
        return routeRepository.findAll();
    }

    /** Saves a new route record to the database. */
    public Route createRoute(Route route) {
        if (route == null) throw new IllegalArgumentException("Route cannot be null");
        return routeRepository.save(route);
    }

    public Route getById(String id) {
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("Route id cannot be empty");
        }
        return routeRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Ruta no encontrada"));
    }

    public Route updateRoute(String id, Route payload) {
        Route current = getById(id);
        if (payload == null) {
            throw new IllegalArgumentException("Route cannot be null");
        }

        if (payload.getName() != null) {
            current.setName(payload.getName());
        }
        if (payload.getStatus() != null) {
            current.setStatus(payload.getStatus());
        }
        if (payload.getDate() != null) {
            current.setDate(payload.getDate());
        }
        if (payload.getDistanceKm() != null) {
            current.setDistanceKm(payload.getDistanceKm());
        }
        if (payload.getAssignedDriver() != null) {
            current.setAssignedDriver(payload.getAssignedDriver());
        }

        return routeRepository.save(current);
    }

    public void deleteRoute(String id) {
        getById(id);
        routeRepository.deleteById(id);
    }

    public List<Map<String, Object>> getStops(String id) {
        Route route = getById(id);

        Map<String, double[]> coords = new LinkedHashMap<>();
        coords.put("HospitalSanRafael", new double[] { 4.710989, -74.072090 });
        coords.put("ClinicaSur", new double[] { 4.609710, -74.081750 });
        coords.put("LaboratorioCentral", new double[] { 4.648283, -74.247894 });
        coords.put("CentroVeterinario", new double[] { 4.735000, -74.070000 });
        coords.put("HospitalPediatrico", new double[] { 4.676000, -74.048000 });
        coords.put("DepositoCentral", new double[] { 4.598100, -74.075800 });

        String start = coords.containsKey(route.getName()) ? route.getName() : "HospitalSanRafael";
        List<String> path = routeGraph.dijkstra(start, "DepositoCentral").path;
        if (path == null || path.isEmpty()) {
            path = List.of(start, "DepositoCentral");
        }

        return path.stream()
            .map(name -> {
                double[] c = coords.getOrDefault(name, new double[] { 0.0, 0.0 });
                Map<String, Object> m = new LinkedHashMap<>();
                m.put("name", name);
                m.put("lat", c[0]);
                m.put("lng", c[1]);
                return m;
            })
            .toList();
    }
}


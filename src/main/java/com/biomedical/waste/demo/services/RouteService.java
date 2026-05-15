package com.biomedical.waste.demo.services;

import com.biomedical.waste.demo.models.Route;
import com.biomedical.waste.demo.models.RouteStop;
import com.biomedical.waste.demo.models.RouteStopStage;
import com.biomedical.waste.demo.repository.RouteRepository;
import com.biomedical.waste.demo.repository.RouteStopRepository;
import com.biomedical.waste.demo.structures.RouteGraph;
import jakarta.annotation.PostConstruct;
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
    private final RouteStopRepository routeStopRepository;
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
        if (route.getCode() == null || route.getCode().isBlank()) {
            route.setCode("R-" + java.util.UUID.randomUUID().toString().substring(0, 8).toUpperCase());
        }
        if (route.getStatus() == null || route.getStatus().isBlank()) {
            route.setStatus("PENDING");
        }
        if (route.getDate() == null) {
            route.setDate(java.time.LocalDate.now());
        }
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
        if (payload.getCode() != null) {
            current.setCode(payload.getCode());
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

    public List<RouteStop> listStops(String routeId) {
        getById(routeId);
        return routeStopRepository.findByRouteIdOrderByCreatedAtAsc(routeId);
    }

    public RouteStop createStop(String routeId, RouteStop stop) {
        getById(routeId);
        if (stop == null) {
            throw new IllegalArgumentException("Stop cannot be null");
        }
        if (stop.getStage() == null) {
            stop.setStage(RouteStopStage.GENERATED);
        }
        stop.setId(null);
        stop.setRouteId(routeId);
        return routeStopRepository.save(stop);
    }

    public RouteStop updateStop(String routeId, String stopId, RouteStop payload) {
        getById(routeId);
        if (payload == null) {
            throw new IllegalArgumentException("Stop cannot be null");
        }
        RouteStop current = routeStopRepository.findById(stopId)
            .filter(s -> routeId.equals(s.getRouteId()))
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Parada no encontrada"));

        if (payload.getName() != null) {
            current.setName(payload.getName());
        }
        current.setLat(payload.getLat());
        current.setLng(payload.getLng());
        if (payload.getStage() != null) {
            current.setStage(payload.getStage());
        }

        return routeStopRepository.save(current);
    }

    public void deleteStop(String routeId, String stopId) {
        getById(routeId);
        RouteStop current = routeStopRepository.findById(stopId)
            .filter(s -> routeId.equals(s.getRouteId()))
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Parada no encontrada"));
        routeStopRepository.delete(current);
    }
}

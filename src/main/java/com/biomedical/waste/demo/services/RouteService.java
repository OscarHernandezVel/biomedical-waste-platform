package com.biomedical.waste.demo.services;

import com.biomedical.waste.demo.models.Route;
import com.biomedical.waste.demo.repository.RouteRepository;
import com.biomedical.waste.demo.structures.RouteGraph;
import jakarta.annotation.PostConstruct;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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
}


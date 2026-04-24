package com.biomedical.waste.demo.structures;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;
import java.util.Comparator;

/** Undirected weighted graph representing collection points and routes between them. */
public class RouteGraph {

    /** Represents a weighted edge between two collection points. */
    public static class Edge {
        public String destination;
        public double distanceKm;
        public int estimatedMinutes;
        public Edge(String destination, double distanceKm, int estimatedMinutes) {
            this.destination = destination;
            this.distanceKm = distanceKm;
            this.estimatedMinutes = estimatedMinutes;
        }
    }

    /** Holds the result of a shortest path calculation. */
    public static class PathResult {
        public List<String> path;
        public double totalDistanceKm;
        public int totalMinutes;
        public PathResult(List<String> path, double totalDistanceKm, int totalMinutes) {
            this.path = path;
            this.totalDistanceKm = totalDistanceKm;
            this.totalMinutes = totalMinutes;
        }
    }

    private final Map<String, List<Edge>> adjacencyList = new HashMap<>();

    /** Adds a new collection point (node) to the route network. */
    public void addNode(String location) {
        adjacencyList.computeIfAbsent(location, k -> new ArrayList<>());
    }

    /** Adds a bidirectional route between two collection points with distance and time. */
    public void addRoute(String from, String to, double distanceKm, int minutes) {
        addNode(from);
        addNode(to);
        adjacencyList.get(from).add(new Edge(to, distanceKm, minutes));
        adjacencyList.get(to).add(new Edge(from, distanceKm, minutes));
    }

    /** Removes a collection point and all its connected routes. */
    public void removeNode(String location) {
        List<Edge> neighbors = adjacencyList.remove(location);
        if (neighbors != null) {
            for (Edge e : neighbors) {
                List<Edge> list = adjacencyList.get(e.destination);
                if (list != null) {
                    list.removeIf(edge -> edge.destination.equals(location));
                }
            }
        }
    }

    /**
     * Finds the shortest path between two points using Dijkstra's algorithm.
     * Throws IllegalArgumentException if source or destination do not exist.
     * Throws RuntimeException("No path found") if destination is unreachable.
     */
    public PathResult dijkstra(String source, String destination) {
        if (!adjacencyList.containsKey(source) || !adjacencyList.containsKey(destination)) {
            throw new IllegalArgumentException("Source or destination does not exist");
        }
        Map<String, Double> dist = new HashMap<>();
        Map<String, Integer> time = new HashMap<>();
        Map<String, String> prev = new HashMap<>();
        for (String node : adjacencyList.keySet()) {
            dist.put(node, Double.POSITIVE_INFINITY);
            time.put(node, Integer.MAX_VALUE);
        }
        dist.put(source, 0.0);
        time.put(source, 0);

        PriorityQueue<String> pq = new PriorityQueue<>(Comparator.comparingDouble(dist::get));
        pq.add(source);

        Set<String> visited = new HashSet<>();
        while (!pq.isEmpty()) {
            String u = pq.poll();
            if (!visited.add(u)) continue;
            if (u.equals(destination)) break;
            for (Edge e : adjacencyList.getOrDefault(u, List.of())) {
                String v = e.destination;
                double altDist = dist.get(u) + e.distanceKm;
                int altTime = time.get(u) + e.estimatedMinutes;
                if (altDist < dist.get(v)) {
                    dist.put(v, altDist);
                    time.put(v, altTime);
                    prev.put(v, u);
                    pq.add(v);
                }
            }
        }
        if (!prev.containsKey(destination) && !source.equals(destination)) {
            throw new RuntimeException("No path found");
        }
        List<String> path = new ArrayList<>();
        String curr = destination;
        path.add(curr);
        while (prev.containsKey(curr)) {
            curr = prev.get(curr);
            path.add(0, curr);
        }
        return new PathResult(path, dist.get(destination), time.get(destination));
    }

    /** Returns all nodes reachable from the start node using BFS. */
    public List<String> bfs(String start) {
        if (!adjacencyList.containsKey(start)) return List.of();
        List<String> order = new ArrayList<>();
        Set<String> visited = new HashSet<>();
        Queue<String> q = new ArrayDeque<>();
        visited.add(start);
        q.add(start);
        while (!q.isEmpty()) {
            String u = q.poll();
            order.add(u);
            for (Edge e : adjacencyList.getOrDefault(u, List.of())) {
                if (!visited.contains(e.destination)) {
                    visited.add(e.destination);
                    q.add(e.destination);
                }
            }
        }
        return order;
    }

    /** Returns nodes visited in depth-first order from the start node. */
    public List<String> dfs(String start) {
        if (!adjacencyList.containsKey(start)) return List.of();
        List<String> order = new ArrayList<>();
        Set<String> visited = new HashSet<>();
        dfsRec(start, visited, order);
        return order;
    }

    private void dfsRec(String node, Set<String> visited, List<String> order) {
        visited.add(node);
        order.add(node);
        for (Edge e : adjacencyList.getOrDefault(node, List.of())) {
            if (!visited.contains(e.destination)) {
                dfsRec(e.destination, visited, order);
            }
        }
    }

    /** Returns true if the given location exists in the route network. */
    public boolean hasNode(String location) {
        return adjacencyList.containsKey(location);
    }

    /** Returns the list of direct neighbors for the given location. */
    public List<String> getNeighbors(String location) {
        List<String> result = new ArrayList<>();
        for (Edge e : adjacencyList.getOrDefault(location, List.of())) {
            result.add(e.destination);
        }
        return result;
    }

    /** Returns the total number of collection points in the network. */
    public int nodeCount() {
        return adjacencyList.size();
    }

    /** Returns the total number of routes (edges / 2 since undirected). */
    public int edgeCount() {
        int sum = 0;
        for (List<Edge> edges : adjacencyList.values()) {
            sum += edges.size();
        }
        return sum / 2;
    }
}


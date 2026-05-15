package com.biomedical.waste.demo.repository;

import com.biomedical.waste.demo.models.RouteStop;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RouteStopRepository extends JpaRepository<RouteStop, String> {
    List<RouteStop> findByRouteIdOrderByCreatedAtAsc(String routeId);
}


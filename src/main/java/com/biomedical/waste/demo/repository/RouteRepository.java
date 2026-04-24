package com.biomedical.waste.demo.repository;

import com.biomedical.waste.demo.models.Route;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RouteRepository extends JpaRepository<Route, String> {

    /** Finds all route records with the given status. */
    List<Route> findByStatus(String status);

    /** Finds all route records for the given date. */
    List<Route> findByDate(LocalDate date);
}


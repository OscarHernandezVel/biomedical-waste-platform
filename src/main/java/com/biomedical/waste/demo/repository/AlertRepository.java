package com.biomedical.waste.demo.repository;

import com.biomedical.waste.demo.models.Alert;
import com.biomedical.waste.demo.models.AlertLevel;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AlertRepository extends JpaRepository<Alert, String> {

    /** Finds alerts by resolution state. */
    List<Alert> findByResolved(Boolean resolved);

    /** Finds alerts by severity level. */
    List<Alert> findByLevel(AlertLevel level);

    /** Counts alerts by severity level. */
    long countByLevel(AlertLevel level);
}


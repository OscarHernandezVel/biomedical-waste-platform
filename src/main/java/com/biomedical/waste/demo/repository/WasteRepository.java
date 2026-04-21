package com.biomedical.waste.demo.repository;

import com.biomedical.waste.demo.models.Waste;
import com.biomedical.waste.demo.models.WasteStatus;
import com.biomedical.waste.demo.models.WasteType;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WasteRepository extends JpaRepository<Waste, String> {

    /** Finds all wastes of a given type. */
    List<Waste> findByType(WasteType type);

    /** Finds all wastes from a specific originating entity. */
    List<Waste> findByOriginEntity(String originEntity);

    /** Finds all wastes with a given processing status. */
    List<Waste> findByStatus(WasteStatus status);

    /** Returns the 10 most recently generated wastes. */
    List<Waste> findTop10ByOrderByGenerationDateDesc();
}


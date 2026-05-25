package com.biomedical.waste.demo.repository;

import com.biomedical.waste.demo.models.AiInteraction;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AiInteractionRepository extends JpaRepository<AiInteraction, String> {
    List<AiInteraction> findByTypeOrderByCreatedAtDesc(String type);
    List<AiInteraction> findTop50ByOrderByCreatedAtDesc();
}

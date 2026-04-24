package com.biomedical.waste.demo.repository;

import com.biomedical.waste.demo.models.LoteTratamiento;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LoteTratamientoRepository extends JpaRepository<LoteTratamiento, Long> {
    List<LoteTratamiento> findByPlantaId(Long plantaId);
    List<LoteTratamiento> findByTipoTratamiento(String tipoTratamiento);
}


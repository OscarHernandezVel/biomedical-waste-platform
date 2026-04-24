package com.biomedical.waste.demo.repository;

import com.biomedical.waste.demo.models.ManifiestoTransporteRecord;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ManifiestoTransporteRepository extends JpaRepository<ManifiestoTransporteRecord, Long> {
    List<ManifiestoTransporteRecord> findByEstado(String estado);
    List<ManifiestoTransporteRecord> findByOrden_Id(Long ordenId);
}


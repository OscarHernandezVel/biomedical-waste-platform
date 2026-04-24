package com.biomedical.waste.demo.repository;

import com.biomedical.waste.demo.models.OrdenRecoleccion;
import com.biomedical.waste.demo.models.OrdenRecoleccionStatus;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrdenRecoleccionRepository extends JpaRepository<OrdenRecoleccion, Long> {
    OrdenRecoleccion findByCodigoOrden(String codigoOrden);
    List<OrdenRecoleccion> findByStatus(OrdenRecoleccionStatus status);
}


package com.biomedical.waste.demo.repository;

import com.biomedical.waste.demo.models.Ruta;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RutaDbRepository extends JpaRepository<Ruta, Long> {
    List<Ruta> findByEstado(String estado);
    List<Ruta> findByZona(String zona);
}


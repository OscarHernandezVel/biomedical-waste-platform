package com.biomedical.waste.demo.repository;

import com.biomedical.waste.demo.models.EjecucionRuta;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EjecucionRutaRepository extends JpaRepository<EjecucionRuta, Long> {
    List<EjecucionRuta> findByRuta_Id(Long rutaId);
    List<EjecucionRuta> findByVehiculo_Id(Long vehiculoId);
    List<EjecucionRuta> findByEstado(String estado);
}


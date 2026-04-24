package com.biomedical.waste.demo.repository;

import com.biomedical.waste.demo.models.FlotaVehiculo;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FlotaTransporteRepository extends JpaRepository<FlotaVehiculo, Long> {
    List<FlotaVehiculo> findByEstado(String estado);
    FlotaVehiculo findByPlaca(String placa);
}


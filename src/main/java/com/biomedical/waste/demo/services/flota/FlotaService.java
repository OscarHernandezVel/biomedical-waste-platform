package com.biomedical.waste.demo.services.flota;

import com.biomedical.waste.demo.models.FlotaVehiculo;
import com.biomedical.waste.demo.repository.FlotaTransporteRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FlotaService {

    private final FlotaTransporteRepository flotaTransporteRepository;

    public FlotaVehiculo registrar(String placa, String tipoVehiculo, java.math.BigDecimal capacidad) {
        if (placa == null || placa.isBlank()) {
            throw new IllegalArgumentException("placa is required");
        }
        FlotaVehiculo existing = flotaTransporteRepository.findByPlaca(placa);
        if (existing != null) {
            return existing;
        }
        FlotaVehiculo v = FlotaVehiculo.builder()
            .placa(placa)
            .tipoVehiculo(tipoVehiculo)
            .capacidad(capacidad)
            .estado("DISPONIBLE")
            .build();
        return flotaTransporteRepository.save(v);
    }

    public FlotaVehiculo actualizarEstado(Long vehiculoId, String estado) {
        FlotaVehiculo v = flotaTransporteRepository.findById(vehiculoId).orElse(null);
        if (v == null) {
            throw new IllegalArgumentException("vehiculoId not found: " + vehiculoId);
        }
        v.setEstado(estado);
        return flotaTransporteRepository.save(v);
    }

    public List<FlotaVehiculo> estadoActual() {
        return flotaTransporteRepository.findAll();
    }
}

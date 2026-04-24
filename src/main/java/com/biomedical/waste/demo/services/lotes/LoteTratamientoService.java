package com.biomedical.waste.demo.services.lotes;

import com.biomedical.waste.demo.models.LoteTratamiento;
import com.biomedical.waste.demo.models.ManifiestoTransporteRecord;
import com.biomedical.waste.demo.repository.LoteTratamientoRepository;
import com.biomedical.waste.demo.repository.ManifiestoTransporteRepository;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LoteTratamientoService {

    private final LoteTratamientoRepository loteTratamientoRepository;
    private final ManifiestoTransporteRepository manifiestoTransporteRepository;

    public LoteTratamiento crearLote(Long manifiestoId, Long plantaId, String tipoTratamiento) {
        if (manifiestoId == null) {
            throw new IllegalArgumentException("manifiestoId is required");
        }
        if (plantaId == null) {
            throw new IllegalArgumentException("plantaId is required");
        }
        if (tipoTratamiento == null || tipoTratamiento.isBlank()) {
            throw new IllegalArgumentException("tipoTratamiento is required");
        }
        ManifiestoTransporteRecord manifiesto = manifiestoTransporteRepository.findById(manifiestoId)
            .orElseThrow(() -> new IllegalArgumentException("Manifiesto not found: " + manifiestoId));
        LoteTratamiento lote = LoteTratamiento.builder()
            .manifiesto(manifiesto)
            .plantaId(plantaId)
            .tipoTratamiento(tipoTratamiento)
            .fechaInicio(LocalDateTime.now())
            .resultado("PENDIENTE")
            .build();
        return loteTratamientoRepository.save(lote);
    }
}

package com.biomedical.waste.demo.services.manifiestos;

import com.biomedical.waste.demo.models.ManifiestoTransporteRecord;
import com.biomedical.waste.demo.models.OrdenRecoleccion;
import com.biomedical.waste.demo.patterns.factory.manifiesto.ManifiestoFactory;
import com.biomedical.waste.demo.patterns.factory.manifiesto.ManifiestoTransporte;
import com.biomedical.waste.demo.repository.ManifiestoTransporteRepository;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ManifiestoService {

    private final ManifiestoTransporteRepository manifiestoTransporteRepository;

    public ManifiestoTransporteRecord generar(OrdenRecoleccion orden) {
        if (orden == null) {
            throw new IllegalArgumentException("OrdenRecoleccion cannot be null");
        }
        ManifiestoTransporte manifiesto = ManifiestoFactory.create(orden.getTipoResiduo());
        String content = manifiesto.generate(orden);
        ManifiestoTransporteRecord record = ManifiestoTransporteRecord.builder()
            .orden(orden)
            .estado("GENERADO")
            .fechaSalida(LocalDateTime.now())
            .documentType(manifiesto.getDocumentType())
            .content(content)
            .build();
        return manifiestoTransporteRepository.save(record);
    }
}

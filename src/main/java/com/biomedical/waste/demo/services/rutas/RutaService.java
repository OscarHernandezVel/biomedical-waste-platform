package com.biomedical.waste.demo.services.rutas;

import com.biomedical.waste.demo.models.Ruta;
import com.biomedical.waste.demo.repository.RutaDbRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RutaService {

    private final RutaDbRepository rutaDbRepository;

    public List<Ruta> getAll() {
        return rutaDbRepository.findAll();
    }

    public Ruta create(Ruta ruta) {
        if (ruta == null) {
            throw new IllegalArgumentException("Ruta cannot be null");
        }
        return rutaDbRepository.save(ruta);
    }

    public List<Ruta> getByZona(String zona) {
        return rutaDbRepository.findByZona(zona);
    }

    public List<Ruta> getByEstado(String estado) {
        return rutaDbRepository.findByEstado(estado);
    }
}

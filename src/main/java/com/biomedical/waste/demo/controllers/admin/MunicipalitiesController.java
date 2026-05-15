package com.biomedical.waste.demo.controllers.admin;

import com.biomedical.waste.demo.models.admin.Municipality;
import com.biomedical.waste.demo.repository.admin.MunicipalityRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/admin/municipalities")
@RequiredArgsConstructor
public class MunicipalitiesController {

    private final MunicipalityRepository repository;

    @GetMapping
    public ResponseEntity<List<Municipality>> list() {
        return ResponseEntity.ok(repository.findAll());
    }

    @PostMapping
    public ResponseEntity<Municipality> create(@RequestBody Municipality payload) {
        if (payload == null) {
            throw new IllegalArgumentException("Payload cannot be null");
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(repository.save(payload));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Municipality> update(@PathVariable String id, @RequestBody Municipality payload) {
        Municipality current = repository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "No encontrado"));

        if (payload.getName() != null) {
            current.setName(payload.getName());
        }
        if (payload.getRegion() != null) {
            current.setRegion(payload.getRegion());
        }
        if (payload.getHealthInstitutions() != null) {
            current.setHealthInstitutions(payload.getHealthInstitutions());
        }
        if (payload.getMonthlyGeneration() != null) {
            current.setMonthlyGeneration(payload.getMonthlyGeneration());
        }
        if (payload.getLatitude() != null) {
            current.setLatitude(payload.getLatitude());
        }
        if (payload.getLongitude() != null) {
            current.setLongitude(payload.getLongitude());
        }
        current.setActive(payload.isActive());

        return ResponseEntity.ok(repository.save(current));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        if (!repository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No encontrado");
        }
        repository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}


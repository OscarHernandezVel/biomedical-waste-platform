package com.biomedical.waste.demo.controllers.admin;

import com.biomedical.waste.demo.models.admin.WasteLot;
import com.biomedical.waste.demo.repository.admin.WasteLotRepository;
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
@RequestMapping("/api/admin/waste-lots")
@RequiredArgsConstructor
public class WasteLotsController {

    private final WasteLotRepository repository;

    @GetMapping
    public ResponseEntity<List<WasteLot>> list() {
        return ResponseEntity.ok(repository.findAll());
    }

    @PostMapping
    public ResponseEntity<WasteLot> create(@RequestBody WasteLot payload) {
        if (payload == null) {
            throw new IllegalArgumentException("Payload cannot be null");
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(repository.save(payload));
    }

    @PutMapping("/{id}")
    public ResponseEntity<WasteLot> update(@PathVariable String id, @RequestBody WasteLot payload) {
        WasteLot current = repository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "No encontrado"));

        if (payload.getInstitution() != null) {
            current.setInstitution(payload.getInstitution());
        }
        if (payload.getMunicipalityId() != null) {
            current.setMunicipalityId(payload.getMunicipalityId());
        }
        if (payload.getWasteTypeId() != null) {
            current.setWasteTypeId(payload.getWasteTypeId());
        }
        if (payload.getWeightKg() != null) {
            current.setWeightKg(payload.getWeightKg());
        }
        if (payload.getState() != null) {
            current.setState(payload.getState());
        }
        if (payload.getGenerationDate() != null) {
            current.setGenerationDate(payload.getGenerationDate());
        }
        if (payload.getExpirationDate() != null) {
            current.setExpirationDate(payload.getExpirationDate());
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


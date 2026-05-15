package com.biomedical.waste.demo.controllers.admin;

import com.biomedical.waste.demo.models.admin.WasteTypeConfig;
import com.biomedical.waste.demo.repository.admin.WasteTypeConfigRepository;
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
@RequestMapping("/api/admin/waste-types")
@RequiredArgsConstructor
public class WasteTypesController {

    private final WasteTypeConfigRepository repository;

    @GetMapping
    public ResponseEntity<List<WasteTypeConfig>> list() {
        return ResponseEntity.ok(repository.findAll());
    }

    @PostMapping
    public ResponseEntity<WasteTypeConfig> create(@RequestBody WasteTypeConfig payload) {
        if (payload == null) {
            throw new IllegalArgumentException("Payload cannot be null");
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(repository.save(payload));
    }

    @PutMapping("/{id}")
    public ResponseEntity<WasteTypeConfig> update(@PathVariable String id, @RequestBody WasteTypeConfig payload) {
        WasteTypeConfig current = repository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "No encontrado"));

        if (payload.getCode() != null) {
            current.setCode(payload.getCode());
        }
        if (payload.getName() != null) {
            current.setName(payload.getName());
        }
        if (payload.getRiskLevel() != null) {
            current.setRiskLevel(payload.getRiskLevel());
        }
        if (payload.getMaxStorageDays() != null) {
            current.setMaxStorageDays(payload.getMaxStorageDays());
        }
        if (payload.getTreatment() != null) {
            current.setTreatment(payload.getTreatment());
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


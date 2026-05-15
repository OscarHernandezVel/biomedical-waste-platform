package com.biomedical.waste.demo.controllers.admin;

import com.biomedical.waste.demo.models.admin.DepartmentalRouteConfig;
import com.biomedical.waste.demo.repository.admin.DepartmentalRouteRepository;
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
@RequestMapping("/api/admin/departmental-routes")
@RequiredArgsConstructor
public class DepartmentalRoutesController {

    private final DepartmentalRouteRepository repository;

    @GetMapping
    public ResponseEntity<List<DepartmentalRouteConfig>> list() {
        return ResponseEntity.ok(repository.findAll());
    }

    @PostMapping
    public ResponseEntity<DepartmentalRouteConfig> create(@RequestBody DepartmentalRouteConfig payload) {
        if (payload == null) {
            throw new IllegalArgumentException("Payload cannot be null");
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(repository.save(payload));
    }

    @PutMapping("/{id}")
    public ResponseEntity<DepartmentalRouteConfig> update(@PathVariable String id, @RequestBody DepartmentalRouteConfig payload) {
        DepartmentalRouteConfig current = repository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "No encontrado"));

        if (payload.getName() != null) {
            current.setName(payload.getName());
        }
        if (payload.getFleetId() != null) {
            current.setFleetId(payload.getFleetId());
        }
        if (payload.getMunicipalitiesIds() != null) {
            current.setMunicipalitiesIds(payload.getMunicipalitiesIds());
        }
        if (payload.getSchedule() != null) {
            current.setSchedule(payload.getSchedule());
        }
        if (payload.getProgress() != null) {
            current.setProgress(payload.getProgress());
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


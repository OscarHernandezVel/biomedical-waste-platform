package com.biomedical.waste.demo.controllers.admin;

import com.biomedical.waste.demo.models.admin.TransportFleetVehicle;
import com.biomedical.waste.demo.repository.admin.TransportFleetRepository;
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
@RequestMapping("/api/admin/transport-fleet")
@RequiredArgsConstructor
public class TransportFleetController {

    private final TransportFleetRepository repository;

    @GetMapping
    public ResponseEntity<List<TransportFleetVehicle>> list() {
        return ResponseEntity.ok(repository.findAll());
    }

    @PostMapping
    public ResponseEntity<TransportFleetVehicle> create(@RequestBody TransportFleetVehicle payload) {
        if (payload == null) {
            throw new IllegalArgumentException("Payload cannot be null");
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(repository.save(payload));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TransportFleetVehicle> update(@PathVariable String id, @RequestBody TransportFleetVehicle payload) {
        TransportFleetVehicle current = repository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "No encontrado"));

        if (payload.getPlate() != null) {
            current.setPlate(payload.getPlate());
        }
        if (payload.getBrand() != null) {
            current.setBrand(payload.getBrand());
        }
        if (payload.getCapacityTons() != null) {
            current.setCapacityTons(payload.getCapacityTons());
        }
        if (payload.getDriver() != null) {
            current.setDriver(payload.getDriver());
        }
        if (payload.getState() != null) {
            current.setState(payload.getState());
        }
        if (payload.getLastMaintenance() != null) {
            current.setLastMaintenance(payload.getLastMaintenance());
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


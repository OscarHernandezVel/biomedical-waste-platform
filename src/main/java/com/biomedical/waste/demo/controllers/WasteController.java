package com.biomedical.waste.demo.controllers;

import com.biomedical.waste.demo.models.Waste;
import com.biomedical.waste.demo.models.WasteType;
import com.biomedical.waste.demo.services.WasteService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/wastes")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class WasteController {

    private final WasteService wasteService;

    /** Returns all registered waste items. */
    @GetMapping
    public ResponseEntity<List<Waste>> getAll() {
        return ResponseEntity.ok(wasteService.getAll());
    }

    /** Returns a single waste item by its id. */
    @GetMapping("/{id}")
    public ResponseEntity<Waste> getById(@PathVariable String id) {
        return ResponseEntity.ok(wasteService.getById(id));
    }

    /** Creates a new waste record with full validation. */
    @PostMapping
    public ResponseEntity<Waste> create(@RequestBody Waste waste) {
        return ResponseEntity.ok(wasteService.create(waste));
    }

    /** Updates an existing waste record. */
    @PutMapping("/{id}")
    public ResponseEntity<Waste> update(@PathVariable String id, @RequestBody Waste waste) {
        return ResponseEntity.ok(wasteService.update(id, waste));
    }

    /** Deletes a waste record by id. */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        wasteService.delete(id);
        return ResponseEntity.noContent().build();
    }

    /** Returns all wastes filtered by type (e.g. INFECTIOUS). */
    @GetMapping("/type/{type}")
    public ResponseEntity<List<Waste>> getByType(@PathVariable WasteType type) {
        return ResponseEntity.ok(wasteService.getByType(type));
    }

    /** Returns all wastes from a specific originating entity. */
    @GetMapping("/entity/{entity}")
    public ResponseEntity<List<Waste>> getByEntity(@PathVariable String entity) {
        return ResponseEntity.ok(wasteService.getByEntity(entity));
    }

    /** Returns the total weight in kg of all registered waste. */
    @GetMapping("/total-weight")
    public ResponseEntity<Double> getTotalWeight() {
        return ResponseEntity.ok(wasteService.getTotalWeight());
    }

    /** Processes a waste item through the full decorator chain. */
    @PostMapping("/{id}/process")
    public ResponseEntity<Waste> processWaste(@PathVariable String id) {
        return ResponseEntity.ok(wasteService.processWaste(id));
    }

    /** Clones a waste item using the Prototype pattern. */
    @PostMapping("/{id}/clone")
    public ResponseEntity<Waste> cloneWaste(@PathVariable String id) {
        return ResponseEntity.ok(wasteService.cloneWaste(id));
    }

    /** Advances the waste status to the next step in the workflow. */
    @PutMapping("/{id}/advance-status")
    public ResponseEntity<Waste> advanceStatus(@PathVariable String id) {
        return ResponseEntity.ok(wasteService.advanceStatus(id));
    }

    /** Returns the global traceability log for all waste operations. */
    @GetMapping("/trace-log")
    public ResponseEntity<List<String>> getTraceLog() {
        return ResponseEntity.ok(wasteService.getTraceLog());
    }

    /** Returns waste weights sorted ascending using the BST inorder traversal. */
    @GetMapping("/weights-sorted")
    public ResponseEntity<List<Double>> getWeightsSorted() {
        return ResponseEntity.ok(wasteService.getWeightsSorted());
    }
}


package com.biomedical.waste.demo.controllers;

import com.biomedical.waste.demo.controllers.dto.AlertDto;
import com.biomedical.waste.demo.models.Alert;
import com.biomedical.waste.demo.models.AlertLevel;
import com.biomedical.waste.demo.models.Waste;
import com.biomedical.waste.demo.services.AlertService;
import com.biomedical.waste.demo.services.WasteService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/alerts")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class AlertController {

    private final AlertService alertService;
    private final WasteService wasteService;

    /** Returns all currently active (unresolved) alerts. */
    @GetMapping
    public ResponseEntity<List<AlertDto>> getActive() {
        return ResponseEntity.ok(alertService.getActive().stream().map(AlertController::toDto).toList());
    }

    /** Returns the full alert history from the stack (most recent first). */
    @GetMapping("/history")
    public ResponseEntity<List<AlertDto>> getHistory() {
        return ResponseEntity.ok(alertService.getHistory().stream().map(AlertController::toDto).toList());
    }

    /** Generates an alert for the specified waste item. */
    @PostMapping("/generate/{wasteId}")
    public ResponseEntity<AlertDto> generateAlert(@PathVariable String wasteId) {
        Waste waste = wasteService.getById(wasteId);
        return ResponseEntity.ok(toDto(alertService.generateAlert(waste)));
    }

    /** Resolves the most recent alert by popping it from the history stack. */
    @PutMapping("/resolve-latest")
    public ResponseEntity<AlertDto> resolveLatest() {
        return ResponseEntity.ok(toDto(alertService.resolveLatest()));
    }

    /** Returns the number of alerts for the given severity level. */
    @GetMapping("/count/{level}")
    public ResponseEntity<Long> countByLevel(@PathVariable AlertLevel level) {
        return ResponseEntity.ok(alertService.countByLevel(level));
    }

    @PatchMapping("/{id}/read")
    public ResponseEntity<AlertDto> markRead(@PathVariable String id) {
        return ResponseEntity.ok(toDto(alertService.markRead(id)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        alertService.delete(id);
        return ResponseEntity.noContent().build();
    }

    private static AlertDto toDto(Alert alert) {
        String level = alert.getLevel() == null ? null : alert.getLevel().name().toLowerCase();
        String createdAt = alert.getDate() == null ? null : alert.getDate().toString();
        String entityId = alert.getWasteId() == null ? null : "waste-" + alert.getWasteId();
        boolean resolved = Boolean.TRUE.equals(alert.getResolved());
        return new AlertDto(alert.getId(), level, alert.getMessage(), createdAt, entityId, resolved);
    }
}


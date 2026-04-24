package com.biomedical.waste.demo.controllers;

import com.biomedical.waste.demo.models.Alert;
import com.biomedical.waste.demo.models.AlertLevel;
import com.biomedical.waste.demo.models.Waste;
import com.biomedical.waste.demo.services.AlertService;
import com.biomedical.waste.demo.services.WasteService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
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
    public ResponseEntity<List<Alert>> getActive() {
        return ResponseEntity.ok(alertService.getActive());
    }

    /** Returns the full alert history from the stack (most recent first). */
    @GetMapping("/history")
    public ResponseEntity<List<Alert>> getHistory() {
        return ResponseEntity.ok(alertService.getHistory());
    }

    /** Generates an alert for the specified waste item. */
    @PostMapping("/generate/{wasteId}")
    public ResponseEntity<Alert> generateAlert(@PathVariable String wasteId) {
        Waste waste = wasteService.getById(wasteId);
        return ResponseEntity.ok(alertService.generateAlert(waste));
    }

    /** Resolves the most recent alert by popping it from the history stack. */
    @PutMapping("/resolve-latest")
    public ResponseEntity<Alert> resolveLatest() {
        return ResponseEntity.ok(alertService.resolveLatest());
    }

    /** Returns the number of alerts for the given severity level. */
    @GetMapping("/count/{level}")
    public ResponseEntity<Long> countByLevel(@PathVariable AlertLevel level) {
        return ResponseEntity.ok(alertService.countByLevel(level));
    }
}


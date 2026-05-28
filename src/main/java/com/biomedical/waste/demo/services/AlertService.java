package com.biomedical.waste.demo.services;

import com.biomedical.waste.demo.models.Alert;
import com.biomedical.waste.demo.models.AlertLevel;
import com.biomedical.waste.demo.models.Waste;
import com.biomedical.waste.demo.patterns.abstractfactory.NotificationFactory;
import com.biomedical.waste.demo.repository.AlertRepository;
import com.biomedical.waste.demo.structures.AlertStack;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class AlertService {

    private final AlertRepository alertRepository;
    private final AlertStack<Alert> alertStack = new AlertStack<>();

    /** Generates an alert for a waste item based on its risk level and sends notifications. */
    public Alert generateAlert(Waste waste) {
        if (waste == null) {
            throw new IllegalArgumentException("Waste cannot be null");
        }
        AlertLevel level = determineLevel(waste);
        String typeLabel = switch (waste.getType()) {
            case INFECTIOUS -> "Infeccioso";
            case SHARPS -> "Cortopunzante";
            case CHEMICAL -> "Químico";
            case PHARMACEUTICAL -> "Farmacéutico";
            case ANATOMICAL -> "Anatómico";
        };
        Alert alert = Alert.builder()
            .message("Riesgo detectado: Residuo " + typeLabel
                + " (" + waste.getWeightKg() + " kg) de " + waste.getOriginEntity()
                + " — Nivel de riesgo: " + waste.getType().getRiskLevel())
            .level(level)
            .wasteId(waste.getId())
            .resolved(false)
            .build();

        NotificationFactory factory = NotificationFactory.forLevel(level);
        factory.createSystemAlert().log("Alert generated for waste " + waste.getId(), level);
        if (level == AlertLevel.HIGH) {
            factory.createEmailAlert().send(
                "admin@biomedical.com",
                "High Risk Waste Alert",
                "Waste " + waste.getId() + " requires immediate action."
            );
        }

        Alert saved = alertRepository.save(alert);
        alertStack.push(saved);
        return saved;
    }

    /** Resolves the most recent unresolved alert by popping it from the history stack. */
    public Alert resolveLatest() {
        Alert alert = alertStack.pop();
        alert.resolveAlert();
        return alertRepository.save(alert);
    }

    /** Returns all currently unresolved alerts. */
    public List<Alert> getActive() {
        return alertRepository.findByResolved(false);
    }

    public Alert markRead(String id) {
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("Alert id cannot be empty");
        }
        Alert alert = alertRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Alerta no encontrada"));
        alert.setResolved(true);
        return alertRepository.save(alert);
    }

    public void delete(String id) {
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("Alert id cannot be empty");
        }
        if (!alertRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Alerta no encontrada");
        }
        alertRepository.deleteById(id);
    }

    /** Returns the full alert history from the stack (most recent first). */
    public List<Alert> getHistory() {
        return alertStack.toList();
    }

    /** Returns the number of alerts for the given severity level. */
    public long countByLevel(AlertLevel level) {
        if (level == null) {
            throw new IllegalArgumentException("Alert level cannot be null");
        }
        return alertRepository.countByLevel(level);
    }

    private AlertLevel determineLevel(Waste waste) {
        if (waste == null || waste.getType() == null) {
            throw new IllegalArgumentException("Waste type is required to determine alert level");
        }
        int risk = waste.getType().getRiskLevel();
        if (risk >= 4) return AlertLevel.HIGH;
        if (risk == 3) return AlertLevel.MEDIUM;
        return AlertLevel.LOW;
    }
}

package com.biomedical.waste.demo.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "alerts")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Alert {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(nullable = false)
    private String message;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AlertLevel level;

    @Column(nullable = false)
    private LocalDateTime date;

    @Column(nullable = false)
    private Boolean resolved;

    @Column(name = "waste_id")
    private String wasteId;

    @PrePersist
    private void prePersist() {
        if (date == null) {
            date = LocalDateTime.now();
        }
        if (resolved == null) {
            resolved = false;
        }
    }

    /** Marks this alert as resolved and returns itself. */
    public Alert resolveAlert() {
        this.resolved = true;
        return this;
    }
}


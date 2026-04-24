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
@Table(name = "wastes")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Waste implements Cloneable {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private WasteType type;

    @Column(name = "weight_kg", nullable = false)
    private Double weightKg;

    @Column(name = "origin_entity", nullable = false)
    private String originEntity;

    @Column(name = "generation_date")
    private LocalDateTime generationDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private WasteStatus status;

    @Column(name = "treatment_method")
    private String treatmentMethod;

    @PrePersist
    private void prePersist() {
        if (generationDate == null) {
            generationDate = LocalDateTime.now();
        }
        if (status == null) {
            status = WasteStatus.PENDING;
        }
    }

    /** Returns a shallow clone of this waste entity. */
    public Waste clone() {
        try {
            return (Waste) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new IllegalStateException("Cloning is not supported for Waste", e);
        }
    }
}


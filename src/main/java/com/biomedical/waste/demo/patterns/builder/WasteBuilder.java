package com.biomedical.waste.demo.patterns.builder;

import com.biomedical.waste.demo.models.Waste;
import com.biomedical.waste.demo.models.WasteStatus;
import com.biomedical.waste.demo.models.WasteType;
import java.time.LocalDateTime;

public class WasteBuilder {
    private WasteType type;
    private Double weightKg;
    private String originEntity;
    private LocalDateTime generationDate;
    private WasteStatus status;

    /** Sets the waste type. Required. */
    public WasteBuilder withType(WasteType type) {
        this.type = type;
        return this;
    }

    /** Sets the weight in kg and throws IllegalArgumentException if the value is invalid. */
    public WasteBuilder withWeight(Double weightKg) {
        if (weightKg == null || weightKg <= 0) {
            throw new IllegalArgumentException("Weight must be greater than zero");
        }
        this.weightKg = weightKg;
        return this;
    }

    /** Sets the originating entity and throws IllegalArgumentException if blank. */
    public WasteBuilder withOriginEntity(String entity) {
        if (entity == null || entity.isBlank()) {
            throw new IllegalArgumentException("Origin entity cannot be blank");
        }
        this.originEntity = entity;
        return this;
    }

    /** Sets the generation date and defaults to now if null. */
    public WasteBuilder withDate(LocalDateTime date) {
        this.generationDate = (date != null) ? date : LocalDateTime.now();
        return this;
    }

    /** Sets the waste status and defaults to PENDING if null. */
    public WasteBuilder withStatus(WasteStatus status) {
        this.status = (status != null) ? status : WasteStatus.PENDING;
        return this;
    }

    /** Builds and returns the Waste object and throws IllegalStateException if required fields are missing. */
    public Waste build() {
        if (type == null) {
            throw new IllegalStateException("type is required");
        }
        if (originEntity == null || originEntity.isBlank()) {
            throw new IllegalStateException("originEntity is required");
        }
        if (generationDate == null) {
            generationDate = LocalDateTime.now();
        }
        if (status == null) {
            status = WasteStatus.PENDING;
        }
        return Waste.builder()
            .type(type)
            .weightKg(weightKg)
            .originEntity(originEntity)
            .generationDate(generationDate)
            .status(status)
            .build();
    }
}


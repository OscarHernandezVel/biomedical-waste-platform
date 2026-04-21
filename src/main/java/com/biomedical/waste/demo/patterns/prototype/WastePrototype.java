package com.biomedical.waste.demo.patterns.prototype;

import com.biomedical.waste.demo.models.Waste;
import com.biomedical.waste.demo.models.WasteStatus;
import com.biomedical.waste.demo.models.WasteType;
import java.time.LocalDateTime;
import java.util.UUID;

public class WastePrototype {
    private final Waste original;

    /** Creates a prototype wrapper for a waste entity to generate safe copies. */
    public WastePrototype(Waste original) {
        this.original = original;
    }

    /** Creates an exact deep copy of the original waste with a new UUID. */
    public Waste clone() {
        return buildCopy(
            original.getType(),
            original.getOriginEntity(),
            original.getGenerationDate(),
            original.getStatus()
        );
    }

    /** Creates a copy with generation date set to now as a new collection record. */
    public Waste cloneWithNewDate() {
        return buildCopy(
            original.getType(),
            original.getOriginEntity(),
            LocalDateTime.now(),
            original.getStatus()
        );
    }

    /** Creates a copy assigned to a different originating entity. */
    public Waste cloneForEntity(String newEntity) {
        return buildCopy(
            original.getType(),
            newEntity,
            LocalDateTime.now(),
            WasteStatus.PENDING
        );
    }

    /** Creates a copy with an updated workflow status. */
    public Waste cloneWithStatus(WasteStatus newStatus) {
        return buildCopy(
            original.getType(),
            original.getOriginEntity(),
            original.getGenerationDate(),
            newStatus
        );
    }

    private Waste buildCopy(WasteType type, String entity, LocalDateTime date, WasteStatus status) {
        return Waste.builder()
            .id(UUID.randomUUID().toString())
            .type(type)
            .weightKg(original.getWeightKg())
            .originEntity(entity)
            .generationDate(date)
            .status(status)
            .treatmentMethod(original.getTreatmentMethod())
            .build();
    }
}


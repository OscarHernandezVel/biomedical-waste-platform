package com.biomedical.waste.demo.models.admin;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
@Table(name = "waste_lots")
@Data
public class WasteLot {

    @Id
    @Column(length = 64)
    private String id;

    @JsonProperty("institucion")
    @Column(name = "institucion", nullable = false)
    private String institution;

    @JsonProperty("municipio_id")
    @Column(name = "municipio_id", nullable = false)
    private String municipalityId;

    @JsonProperty("tipo_residuo_id")
    @Column(name = "tipo_residuo_id", nullable = false)
    private String wasteTypeId;

    @JsonProperty("cantidad_kg")
    @Column(name = "cantidad_kg")
    private Double weightKg;

    @JsonProperty("estado")
    @Enumerated(EnumType.STRING)
    @Column(name = "estado", nullable = false)
    private WasteLotState state;

    @JsonProperty("fecha_generacion")
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Column(name = "fecha_generacion")
    private LocalDate generationDate;

    @JsonProperty("fecha_vencimiento")
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Column(name = "fecha_vencimiento")
    private LocalDate expirationDate;

    @JsonProperty("isActive")
    @Column(name = "is_active", nullable = false)
    private boolean active = true;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    void ensureId() {
        if (id == null || id.isBlank()) {
            id = "lot-" + java.util.UUID.randomUUID().toString().substring(0, 8);
        }
        if (state == null) {
            state = WasteLotState.GENERADO;
        }
    }
}


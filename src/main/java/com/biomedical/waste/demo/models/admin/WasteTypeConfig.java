package com.biomedical.waste.demo.models.admin;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
@Table(name = "waste_types")
@Data
public class WasteTypeConfig {

    @Id
    @Column(length = 64)
    private String id;

    @JsonProperty("codigo")
    @Column(name = "codigo", nullable = false)
    private String code;

    @JsonProperty("nombre")
    @Column(name = "nombre", nullable = false)
    private String name;

    @JsonProperty("nivel_riesgo")
    @Enumerated(EnumType.STRING)
    @Column(name = "nivel_riesgo", nullable = false)
    private RiskLevel riskLevel;

    @JsonProperty("dias_almacenamiento_max")
    @Column(name = "dias_almacenamiento_max")
    private Integer maxStorageDays;

    @JsonProperty("tratamiento")
    @Column(name = "tratamiento")
    private String treatment;

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
            id = "wt-" + java.util.UUID.randomUUID().toString().substring(0, 8);
        }
    }
}


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
@Table(name = "municipalities")
@Data
public class Municipality {

    @Id
    @Column(length = 64)
    private String id;

    @JsonProperty("nombre")
    @Column(name = "nombre", nullable = false)
    private String name;

    @JsonProperty("region")
    @Enumerated(EnumType.STRING)
    @Column(name = "region", nullable = false)
    private Region region;

    @JsonProperty("instituciones_salud")
    @Column(name = "instituciones_salud")
    private Integer healthInstitutions;

    @JsonProperty("generacion_mensual")
    @Column(name = "generacion_mensual")
    private Double monthlyGeneration;

    @JsonProperty("latitud")
    @Column(name = "latitud")
    private Double latitude;

    @JsonProperty("longitud")
    @Column(name = "longitud")
    private Double longitude;

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
            id = "mun-" + java.util.UUID.randomUUID().toString().substring(0, 8);
        }
    }
}


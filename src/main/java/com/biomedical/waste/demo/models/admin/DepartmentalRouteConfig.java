package com.biomedical.waste.demo.models.admin;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
@Table(name = "departmental_routes")
@Data
public class DepartmentalRouteConfig {

    @Id
    @Column(length = 64)
    private String id;

    @JsonProperty("nombre")
    @Column(name = "nombre", nullable = false)
    private String name;

    @JsonProperty("flota_id")
    @Column(name = "flota_id")
    private String fleetId;

    @JsonProperty("municipios_ids")
    @ElementCollection
    @CollectionTable(name = "departmental_route_municipios", joinColumns = @JoinColumn(name = "route_id"))
    @Column(name = "municipio_id")
    private List<String> municipalitiesIds = new ArrayList<>();

    @JsonProperty("horario")
    @Column(name = "horario")
    private String schedule;

    @JsonProperty("progreso")
    @Column(name = "progreso")
    private String progress;

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
            id = "droute-" + java.util.UUID.randomUUID().toString().substring(0, 8);
        }
    }
}


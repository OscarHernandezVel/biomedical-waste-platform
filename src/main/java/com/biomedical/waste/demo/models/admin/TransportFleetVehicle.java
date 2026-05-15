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
@Table(name = "transport_fleet")
@Data
public class TransportFleetVehicle {

    @Id
    @Column(length = 64)
    private String id;

    @JsonProperty("placa")
    @Column(name = "placa", nullable = false)
    private String plate;

    @JsonProperty("marca")
    @Column(name = "marca")
    private String brand;

    @JsonProperty("capacidad_toneladas")
    @Column(name = "capacidad_toneladas")
    private Double capacityTons;

    @JsonProperty("conductor")
    @Column(name = "conductor")
    private String driver;

    @JsonProperty("estado")
    @Enumerated(EnumType.STRING)
    @Column(name = "estado", nullable = false)
    private FleetState state;

    @JsonProperty("ultima_mantenimiento")
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Column(name = "ultima_mantenimiento")
    private LocalDate lastMaintenance;

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
            id = "veh-" + java.util.UUID.randomUUID().toString().substring(0, 8);
        }
        if (state == null) {
            state = FleetState.ACTIVO;
        }
    }
}


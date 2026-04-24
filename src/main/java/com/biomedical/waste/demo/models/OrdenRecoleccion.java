package com.biomedical.waste.demo.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "ordenes_recoleccion")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrdenRecoleccion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "codigo_orden", unique = true)
    private String codigoOrden;

    @Column(name = "id_cliente")
    private Long clienteId;

    @ManyToOne
    @JoinColumn(name = "id_ruta")
    private Ruta ruta;

    @Column(name = "fecha_programada")
    private LocalDateTime fechaProgramada;

    @Column(name = "fecha_ejecutada")
    private LocalDateTime fechaEjecutada;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado")
    private OrdenRecoleccionStatus status;

    @Column(name = "tipo_residuo")
    private String tipoResiduo;

    @Column(name = "cantidad_estimada")
    private Double cantidadEstimada;

    @PrePersist
    private void prePersist() {
        if (status == null) {
            status = OrdenRecoleccionStatus.PENDING;
        }
    }

    @PreUpdate
    private void preUpdate() {
        if (status == null) {
            status = OrdenRecoleccionStatus.PENDING;
        }
    }
}

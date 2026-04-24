package com.biomedical.waste.demo.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "manifiestos_transporte")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ManifiestoTransporteRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_orden")
    private OrdenRecoleccion orden;

    @ManyToOne
    @JoinColumn(name = "id_vehiculo")
    private FlotaVehiculo vehiculo;

    @Column(name = "id_conductor")
    private Long conductorId;

    @Column(name = "fecha_salida")
    private LocalDateTime fechaSalida;

    @Column(name = "fecha_llegada")
    private LocalDateTime fechaLlegada;

    @Column(name = "estado")
    private String estado;

    @Transient
    private String documentType;

    @Transient
    private String content;
}

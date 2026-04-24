package com.biomedical.waste.demo.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "flota_transporte")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FlotaVehiculo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String placa;

    @Column(name = "tipo_vehiculo")
    private String tipoVehiculo;

    private BigDecimal capacidad;

    private String estado;
}

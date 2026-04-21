package com.biomedical.waste.demo.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Builder.Default;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "routes")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Route {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    @Default
    private String status = "PENDING";

    @Column(nullable = false)
    private LocalDate date;

    @Column(name = "distance_km")
    private Double distanceKm;

    @Column(name = "assigned_driver")
    private String assignedDriver;

    /** Returns estimated travel time based on average speed of 30 km/h. */
    public int estimatedDurationMinutes() {
        return (int) ((distanceKm / 30.0) * 60);
    }
}

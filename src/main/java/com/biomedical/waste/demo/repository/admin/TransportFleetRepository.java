package com.biomedical.waste.demo.repository.admin;

import com.biomedical.waste.demo.models.admin.TransportFleetVehicle;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransportFleetRepository extends JpaRepository<TransportFleetVehicle, String> {}


package com.biomedical.waste.demo.repository.admin;

import com.biomedical.waste.demo.models.admin.DepartmentalRouteConfig;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DepartmentalRouteRepository extends JpaRepository<DepartmentalRouteConfig, String> {}


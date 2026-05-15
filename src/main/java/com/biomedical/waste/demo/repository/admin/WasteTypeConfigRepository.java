package com.biomedical.waste.demo.repository.admin;

import com.biomedical.waste.demo.models.admin.WasteTypeConfig;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WasteTypeConfigRepository extends JpaRepository<WasteTypeConfig, String> {}


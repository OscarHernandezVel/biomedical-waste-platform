package com.biomedical.waste.demo.repository.admin;

import com.biomedical.waste.demo.models.admin.WasteLot;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WasteLotRepository extends JpaRepository<WasteLot, String> {}


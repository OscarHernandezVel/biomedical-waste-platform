package com.biomedical.waste.demo.repository.admin;

import com.biomedical.waste.demo.models.admin.Municipality;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MunicipalityRepository extends JpaRepository<Municipality, String> {}


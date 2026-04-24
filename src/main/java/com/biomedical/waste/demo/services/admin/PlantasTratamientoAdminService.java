package com.biomedical.waste.demo.services.admin;

import javax.sql.DataSource;
import org.springframework.stereotype.Service;

@Service
public class PlantasTratamientoAdminService extends AdminTableCrudService {
    public PlantasTratamientoAdminService(DataSource dataSource) {
        super(dataSource, TableSelector.PLANTAS_TRATAMIENTO);
    }
}


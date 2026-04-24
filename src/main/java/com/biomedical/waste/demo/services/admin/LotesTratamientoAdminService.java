package com.biomedical.waste.demo.services.admin;

import javax.sql.DataSource;
import org.springframework.stereotype.Service;

@Service
public class LotesTratamientoAdminService extends AdminTableCrudService {
    public LotesTratamientoAdminService(DataSource dataSource) {
        super(dataSource, TableSelector.LOTES_TRATAMIENTO);
    }
}


package com.biomedical.waste.demo.services.admin;

import javax.sql.DataSource;
import org.springframework.stereotype.Service;

@Service
public class RutasAdminService extends AdminTableCrudService {
    public RutasAdminService(DataSource dataSource) {
        super(dataSource, TableSelector.RUTAS);
    }
}


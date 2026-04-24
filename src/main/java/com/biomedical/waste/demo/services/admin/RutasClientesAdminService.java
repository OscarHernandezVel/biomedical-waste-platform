package com.biomedical.waste.demo.services.admin;

import javax.sql.DataSource;
import org.springframework.stereotype.Service;

@Service
public class RutasClientesAdminService extends AdminTableCrudService {
    public RutasClientesAdminService(DataSource dataSource) {
        super(dataSource, TableSelector.RUTAS_CLIENTES);
    }
}


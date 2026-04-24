package com.biomedical.waste.demo.services.admin;

import javax.sql.DataSource;
import org.springframework.stereotype.Service;

@Service
public class VOrdenesPendientesAdminService extends AdminTableCrudService {
    public VOrdenesPendientesAdminService(DataSource dataSource) {
        super(dataSource, TableSelector.V_ORDENES_PENDIENTES);
    }
}

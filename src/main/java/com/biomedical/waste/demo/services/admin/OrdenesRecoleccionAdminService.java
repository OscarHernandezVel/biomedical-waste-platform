package com.biomedical.waste.demo.services.admin;

import javax.sql.DataSource;
import org.springframework.stereotype.Service;

@Service
public class OrdenesRecoleccionAdminService extends AdminTableCrudService {
    public OrdenesRecoleccionAdminService(DataSource dataSource) {
        super(dataSource, TableSelector.ORDENES_RECOLECCION);
    }
}


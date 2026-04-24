package com.biomedical.waste.demo.services.admin;

import javax.sql.DataSource;
import org.springframework.stereotype.Service;

@Service
public class PrediccionesGeneracionAdminService extends AdminTableCrudService {
    public PrediccionesGeneracionAdminService(DataSource dataSource) {
        super(dataSource, TableSelector.PREDICCIONES_GENERACION);
    }
}


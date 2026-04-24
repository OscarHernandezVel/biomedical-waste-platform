package com.biomedical.waste.demo.services.admin;

import javax.sql.DataSource;
import org.springframework.stereotype.Service;

@Service
public class EjecucionesRutaAdminService extends AdminTableCrudService {
    public EjecucionesRutaAdminService(DataSource dataSource) {
        super(dataSource, TableSelector.EJECUCIONES_RUTA);
    }
}


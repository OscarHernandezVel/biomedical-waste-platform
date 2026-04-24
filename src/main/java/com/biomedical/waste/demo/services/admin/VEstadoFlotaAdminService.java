package com.biomedical.waste.demo.services.admin;

import javax.sql.DataSource;
import org.springframework.stereotype.Service;

@Service
public class VEstadoFlotaAdminService extends AdminTableCrudService {
    public VEstadoFlotaAdminService(DataSource dataSource) {
        super(dataSource, TableSelector.V_ESTADO_FLOTA);
    }
}


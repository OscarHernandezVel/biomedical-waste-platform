package com.biomedical.waste.demo.services.admin;

import javax.sql.DataSource;
import org.springframework.stereotype.Service;

@Service
public class FlotaTransporteAdminService extends AdminTableCrudService {
    public FlotaTransporteAdminService(DataSource dataSource) {
        super(dataSource, TableSelector.FLOTA_TRANSPORTE);
    }
}


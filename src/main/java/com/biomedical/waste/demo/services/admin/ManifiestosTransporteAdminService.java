package com.biomedical.waste.demo.services.admin;

import javax.sql.DataSource;
import org.springframework.stereotype.Service;

@Service
public class ManifiestosTransporteAdminService extends AdminTableCrudService {
    public ManifiestosTransporteAdminService(DataSource dataSource) {
        super(dataSource, TableSelector.MANIFIESTOS_TRANSPORTE);
    }
}


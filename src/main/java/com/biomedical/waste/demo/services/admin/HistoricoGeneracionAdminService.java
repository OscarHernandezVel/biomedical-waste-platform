package com.biomedical.waste.demo.services.admin;

import javax.sql.DataSource;
import org.springframework.stereotype.Service;

@Service
public class HistoricoGeneracionAdminService extends AdminTableCrudService {
    public HistoricoGeneracionAdminService(DataSource dataSource) {
        super(dataSource, TableSelector.HISTORICO_GENERACION);
    }
}


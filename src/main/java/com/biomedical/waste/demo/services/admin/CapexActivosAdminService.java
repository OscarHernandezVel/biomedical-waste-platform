package com.biomedical.waste.demo.services.admin;

import javax.sql.DataSource;
import org.springframework.stereotype.Service;

@Service
public class CapexActivosAdminService extends AdminTableCrudService {
    public CapexActivosAdminService(DataSource dataSource) {
        super(dataSource, TableSelector.CAPEX_ACTIVOS);
    }
}


package com.biomedical.waste.demo.services.admin;

import javax.sql.DataSource;
import org.springframework.stereotype.Service;

@Service
public class DepartamentosAdminService extends AdminTableCrudService {
    public DepartamentosAdminService(DataSource dataSource) {
        super(dataSource, TableSelector.DEPARTAMENTOS);
    }
}


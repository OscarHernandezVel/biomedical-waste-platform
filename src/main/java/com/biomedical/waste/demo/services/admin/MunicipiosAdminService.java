package com.biomedical.waste.demo.services.admin;

import javax.sql.DataSource;
import org.springframework.stereotype.Service;

@Service
public class MunicipiosAdminService extends AdminTableCrudService {
    public MunicipiosAdminService(DataSource dataSource) {
        super(dataSource, TableSelector.MUNICIPIOS);
    }
}


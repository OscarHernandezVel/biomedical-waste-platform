package com.biomedical.waste.demo.services.admin;

import javax.sql.DataSource;
import org.springframework.stereotype.Service;

@Service
public class EppEntregasAdminService extends AdminTableCrudService {
    public EppEntregasAdminService(DataSource dataSource) {
        super(dataSource, TableSelector.EPP_ENTREGAS);
    }
}


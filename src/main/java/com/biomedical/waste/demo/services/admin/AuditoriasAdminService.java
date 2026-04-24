package com.biomedical.waste.demo.services.admin;

import javax.sql.DataSource;
import org.springframework.stereotype.Service;

@Service
public class AuditoriasAdminService extends AdminTableCrudService {
    public AuditoriasAdminService(DataSource dataSource) {
        super(dataSource, TableSelector.AUDITORIAS);
    }
}


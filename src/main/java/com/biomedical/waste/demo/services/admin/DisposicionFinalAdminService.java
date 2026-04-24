package com.biomedical.waste.demo.services.admin;

import javax.sql.DataSource;
import org.springframework.stereotype.Service;

@Service
public class DisposicionFinalAdminService extends AdminTableCrudService {
    public DisposicionFinalAdminService(DataSource dataSource) {
        super(dataSource, TableSelector.DISPOSICION_FINAL);
    }
}


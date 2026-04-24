package com.biomedical.waste.demo.services.admin;

import javax.sql.DataSource;
import org.springframework.stereotype.Service;

@Service
public class PersonalAdminService extends AdminTableCrudService {
    public PersonalAdminService(DataSource dataSource) {
        super(dataSource, TableSelector.PERSONAL);
    }
}


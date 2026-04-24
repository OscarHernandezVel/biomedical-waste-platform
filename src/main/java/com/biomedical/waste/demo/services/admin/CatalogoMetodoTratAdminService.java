package com.biomedical.waste.demo.services.admin;

import javax.sql.DataSource;
import org.springframework.stereotype.Service;

@Service
public class CatalogoMetodoTratAdminService extends AdminTableCrudService {
    public CatalogoMetodoTratAdminService(DataSource dataSource) {
        super(dataSource, TableSelector.CATALOGO_METODO_TRAT);
    }
}


package com.biomedical.waste.demo.services.admin;

import javax.sql.DataSource;
import org.springframework.stereotype.Service;

@Service
public class ClientesInstalacionesAdminService extends AdminTableCrudService {
    public ClientesInstalacionesAdminService(DataSource dataSource) {
        super(dataSource, TableSelector.CLIENTES_INSTALACIONES);
    }
}


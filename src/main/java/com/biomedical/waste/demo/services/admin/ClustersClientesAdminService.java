package com.biomedical.waste.demo.services.admin;

import javax.sql.DataSource;
import org.springframework.stereotype.Service;

@Service
public class ClustersClientesAdminService extends AdminTableCrudService {
    public ClustersClientesAdminService(DataSource dataSource) {
        super(dataSource, TableSelector.CLUSTERS_CLIENTES);
    }
}


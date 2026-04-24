package com.biomedical.waste.demo.services.admin;

import javax.sql.DataSource;
import org.springframework.stereotype.Service;

@Service
public class ClustersGeneradoresAdminService extends AdminTableCrudService {
    public ClustersGeneradoresAdminService(DataSource dataSource) {
        super(dataSource, TableSelector.CLUSTERS_GENERADORES);
    }
}


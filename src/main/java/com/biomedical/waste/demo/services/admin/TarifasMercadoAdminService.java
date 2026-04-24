package com.biomedical.waste.demo.services.admin;

import javax.sql.DataSource;
import org.springframework.stereotype.Service;

@Service
public class TarifasMercadoAdminService extends AdminTableCrudService {
    public TarifasMercadoAdminService(DataSource dataSource) {
        super(dataSource, TableSelector.TARIFAS_MERCADO);
    }
}


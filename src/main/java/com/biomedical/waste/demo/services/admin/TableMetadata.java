package com.biomedical.waste.demo.services.admin;

import java.util.Set;

public record TableMetadata(
    String name,
    String type,
    String primaryKeyColumn,
    Set<String> columns,
    Set<String> autoIncrementColumns
) {
    public boolean isView() {
        return "VIEW".equalsIgnoreCase(type);
    }

    public boolean hasSinglePrimaryKey() {
        return primaryKeyColumn != null && !primaryKeyColumn.isBlank();
    }
}

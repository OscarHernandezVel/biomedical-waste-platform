package com.biomedical.waste.demo.services.admin;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Pattern;
import javax.sql.DataSource;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.web.server.ResponseStatusException;

public abstract class AdminTableCrudService {
    private static final Pattern SAFE_IDENTIFIER = Pattern.compile("[a-zA-Z0-9_]+$");
    private static final int DEFAULT_LIMIT = 100;
    private static final int MAX_LIMIT = 1000;

    private final DataSource dataSource;
    private final NamedParameterJdbcTemplate jdbc;
    private final TableSelector tableSelector;
    private final ConcurrentHashMap<String, TableMetadata> metadataCache = new ConcurrentHashMap<>();

    protected AdminTableCrudService(DataSource dataSource, TableSelector tableSelector) {
        this.dataSource = dataSource;
        this.jdbc = new NamedParameterJdbcTemplate(dataSource);
        this.tableSelector = tableSelector;
    }

    public List<Map<String, Object>> list(Integer limit, Integer offset) {
        TableMetadata meta = resolveTable();
        int resolvedLimit = limit == null ? DEFAULT_LIMIT : Math.min(Math.max(limit, 1), MAX_LIMIT);
        int resolvedOffset = offset == null ? 0 : Math.max(offset, 0);
        String sql = "select * from " + meta.name() + " limit :limit offset :offset";
        return jdbc.queryForList(sql, Map.of("limit", resolvedLimit, "offset", resolvedOffset));
    }

    public Map<String, Object> getById(Object id) {
        TableMetadata meta = resolveTable();
        ensureSinglePrimaryKey(meta);
        String sql = "select * from " + meta.name() + " where " + meta.primaryKeyColumn() + " = :id";
        try {
            return jdbc.queryForMap(sql, Map.of("id", id));
        } catch (EmptyResultDataAccessException ex) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Registro no encontrado");
        }
    }

    public Map<String, Object> create(Map<String, Object> payload) {
        TableMetadata meta = resolveTable();
        ensureWritable(meta);

        Map<String, Object> values = filterColumns(meta, payload);
        if (values.isEmpty()) {
            throw new IllegalArgumentException("No hay campos válidos para insertar");
        }

        List<String> cols = new ArrayList<>(values.keySet());
        String sql = "insert into " + meta.name() + " (" + String.join(",", cols) + ") values (" + cols.stream().map(c -> ":" + c).reduce((a, b) -> a + "," + b).orElse("") + ")";

        KeyHolder keyHolder = new GeneratedKeyHolder();
        MapSqlParameterSource params = new MapSqlParameterSource(values);
        if (meta.hasSinglePrimaryKey()) {
            jdbc.update(sql, params, keyHolder, new String[] { meta.primaryKeyColumn() });
            Object key = keyHolder.getKey();
            Map<String, Object> result = new LinkedHashMap<>();
            result.put("inserted", true);
            if (key != null) {
                result.put(meta.primaryKeyColumn(), key);
            }
            return result;
        }

        int updated = jdbc.update(sql, params);
        return Map.of("inserted", updated > 0);
    }

    public Map<String, Object> update(Object id, Map<String, Object> payload) {
        TableMetadata meta = resolveTable();
        ensureWritable(meta);
        ensureSinglePrimaryKey(meta);

        Map<String, Object> values = filterColumns(meta, payload);
        values.remove(meta.primaryKeyColumn());
        if (values.isEmpty()) {
            throw new IllegalArgumentException("No hay campos válidos para actualizar");
        }

        List<String> sets = values.keySet().stream().map(c -> c + " = :" + c).toList();
        String sql = "update " + meta.name() + " set " + String.join(",", sets) + " where " + meta.primaryKeyColumn() + " = :id";

        MapSqlParameterSource params = new MapSqlParameterSource(values);
        params.addValue("id", id);
        int updated = jdbc.update(sql, params);
        if (updated == 0) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Registro no encontrado");
        }
        return Map.of("updated", true);
    }

    public Map<String, Object> delete(Object id) {
        TableMetadata meta = resolveTable();
        ensureWritable(meta);
        ensureSinglePrimaryKey(meta);

        String sql = "delete from " + meta.name() + " where " + meta.primaryKeyColumn() + " = :id";
        int deleted = jdbc.update(sql, Map.of("id", id));
        if (deleted == 0) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Registro no encontrado");
        }
        return Map.of("deleted", true);
    }

    private void ensureWritable(TableMetadata meta) {
        if (meta.isView()) {
            throw new ResponseStatusException(HttpStatus.METHOD_NOT_ALLOWED, "La vista es de solo lectura");
        }
    }

    private void ensureSinglePrimaryKey(TableMetadata meta) {
        if (!meta.hasSinglePrimaryKey()) {
            throw new ResponseStatusException(HttpStatus.METHOD_NOT_ALLOWED, "La tabla no tiene llave primaria única");
        }
    }

    private Map<String, Object> filterColumns(TableMetadata meta, Map<String, Object> payload) {
        if (payload == null || payload.isEmpty()) {
            return Map.of();
        }
        Map<String, Object> filtered = new LinkedHashMap<>();
        for (Map.Entry<String, Object> e : payload.entrySet()) {
            if (e.getKey() == null) {
                continue;
            }
            String key = e.getKey().trim();
            if (key.isEmpty()) {
                continue;
            }
            if (meta.columns().contains(key)) {
                filtered.put(key, e.getValue());
            }
        }
        return filtered;
    }

    private TableMetadata resolveTable() {
        String cacheKey = tableSelector.value() + "|" + tableSelector.prefix();
        return metadataCache.computeIfAbsent(cacheKey, k -> {
            try (Connection conn = dataSource.getConnection()) {
                DatabaseMetaData dbMeta = conn.getMetaData();
                String resolvedName = resolveTableName(dbMeta, tableSelector);
                if (resolvedName == null) {
                    throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Tabla/Vista no encontrada");
                }
                ensureSafeIdentifier(resolvedName);
                TableInfo info = readTableInfo(dbMeta, conn, resolvedName);
                ensureSafeIdentifier(info.primaryKeyColumn);
                for (String col : info.columns) {
                    ensureSafeIdentifier(col);
                }
                for (String col : info.autoIncrementColumns) {
                    ensureSafeIdentifier(col);
                }
                return new TableMetadata(resolvedName, info.type, info.primaryKeyColumn, Set.copyOf(info.columns), Set.copyOf(info.autoIncrementColumns));
            } catch (SQLException ex) {
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error consultando metadata de la BD");
            }
        });
    }

    private String resolveTableName(DatabaseMetaData dbMeta, TableSelector selector) throws SQLException {
        String needle = selector.value().toLowerCase(Locale.ROOT);
        List<TableName> matches = new ArrayList<>();
        try (ResultSet rs = dbMeta.getTables(null, null, "%", new String[] { "TABLE", "VIEW" })) {
            while (rs.next()) {
                String name = rs.getString("TABLE_NAME");
                String type = rs.getString("TABLE_TYPE");
                if (name == null) {
                    continue;
                }
                String lower = name.toLowerCase(Locale.ROOT);
                boolean match = selector.prefix() ? lower.startsWith(needle) : lower.equals(needle);
                if (match) {
                    matches.add(new TableName(name, type));
                }
            }
        }
        if (matches.isEmpty()) {
            return null;
        }
        if (matches.size() > 1) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "El nombre coincide con múltiples tablas/vistas");
        }
        return matches.getFirst().name;
    }

    private TableInfo readTableInfo(DatabaseMetaData dbMeta, Connection conn, String tableName) throws SQLException {
        String type = null;
        try (ResultSet rs = dbMeta.getTables(conn.getCatalog(), null, tableName, new String[] { "TABLE", "VIEW" })) {
            if (rs.next()) {
                type = rs.getString("TABLE_TYPE");
            }
        }
        if (type == null) {
            try (ResultSet rs = dbMeta.getTables(null, null, tableName, new String[] { "TABLE", "VIEW" })) {
                if (rs.next()) {
                    type = rs.getString("TABLE_TYPE");
                }
            }
        }

        Set<String> columns = new LinkedHashSet<>();
        Set<String> autoIncrementColumns = new LinkedHashSet<>();
        try (ResultSet rs = dbMeta.getColumns(conn.getCatalog(), null, tableName, "%")) {
            while (rs.next()) {
                String col = rs.getString("COLUMN_NAME");
                if (col != null) {
                    columns.add(col);
                }
                String isAuto = rs.getString("IS_AUTOINCREMENT");
                if (col != null && "YES".equalsIgnoreCase(isAuto)) {
                    autoIncrementColumns.add(col);
                }
            }
        }
        if (columns.isEmpty()) {
            try (ResultSet rs = dbMeta.getColumns(null, null, tableName, "%")) {
                while (rs.next()) {
                    String col = rs.getString("COLUMN_NAME");
                    if (col != null) {
                        columns.add(col);
                    }
                    String isAuto = rs.getString("IS_AUTOINCREMENT");
                    if (col != null && "YES".equalsIgnoreCase(isAuto)) {
                        autoIncrementColumns.add(col);
                    }
                }
            }
        }

        String pk = null;
        List<String> pkCols = new ArrayList<>();
        try (ResultSet rs = dbMeta.getPrimaryKeys(conn.getCatalog(), null, tableName)) {
            while (rs.next()) {
                String col = rs.getString("COLUMN_NAME");
                if (col != null) {
                    pkCols.add(col);
                }
            }
        }
        if (pkCols.isEmpty()) {
            try (ResultSet rs = dbMeta.getPrimaryKeys(null, null, tableName)) {
                while (rs.next()) {
                    String col = rs.getString("COLUMN_NAME");
                    if (col != null) {
                        pkCols.add(col);
                    }
                }
            }
        }
        if (pkCols.size() == 1) {
            pk = pkCols.getFirst();
        }

        TableInfo info = new TableInfo();
        info.type = type == null ? "TABLE" : type;
        info.primaryKeyColumn = pk;
        info.columns = columns;
        info.autoIncrementColumns = autoIncrementColumns;
        return info;
    }

    private void ensureSafeIdentifier(String identifier) {
        if (identifier == null || identifier.isBlank()) {
            return;
        }
        if (!SAFE_IDENTIFIER.matcher(identifier).matches()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Identificador inválido");
        }
    }

    private static final class TableInfo {
        private String type;
        private String primaryKeyColumn;
        private Set<String> columns;
        private Set<String> autoIncrementColumns;
    }

    private static final class TableName {
        private final String name;
        private final String type;

        private TableName(String name, String type) {
            this.name = name;
            this.type = type;
        }
    }
}

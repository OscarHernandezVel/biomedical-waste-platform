package com.biomedical.waste.demo.services.admin;

import java.util.List;

public record TableSelector(String value, boolean prefix) {
    public static final TableSelector AUDITORIAS = exact("auditorias");
    public static final TableSelector CAPEX_ACTIVOS = exact("capex_activos");
    public static final TableSelector CATALOGO_METODO_TRAT = prefix("catalogo_metodo_trat");
    public static final TableSelector CATALOGO_TIPO_INSTALACIONES = prefix("catalogo_tipo_instalac");
    public static final TableSelector CATALOGO_TIPO_RESIDUO = exact("catalogo_tipo_residuo");
    public static final TableSelector CLIENTES_INSTALACIONES = exact("clientes_instalaciones");
    public static final TableSelector CLUSTERS_CLIENTES = exact("clusters_clientes");
    public static final TableSelector CLUSTERS_GENERADORES = exact("clusters_generadores");
    public static final TableSelector DEPARTAMENTOS = exact("departamentos");
    public static final TableSelector DISPOSICION_FINAL = exact("disposicion_final");
    public static final TableSelector EJECUCIONES_RUTA = exact("ejecuciones_ruta");
    public static final TableSelector EPP_ENTREGAS = exact("epp_entregas");
    public static final TableSelector FLOTA_TRANSPORTE = exact("flota_transporte");
    public static final TableSelector HISTORICO_GENERACION = exact("historico_generacion");
    public static final TableSelector LOTES_TRATAMIENTO = exact("lotes_tratamiento");
    public static final TableSelector MANIFIESTOS_TRANSPORTE = exact("manifiestos_transporte");
    public static final TableSelector MUNICIPIOS = exact("municipios");
    public static final TableSelector ORDENES_LOTES = exact("ordenes_lotes");
    public static final TableSelector ORDENES_RECOLECCION = exact("ordenes_recoleccion");
    public static final TableSelector PERSONAL = exact("personal");
    public static final TableSelector PLANTAS_TRATAMIENTO = exact("plantas_tratamiento");
    public static final TableSelector PREDICCIONES_GENERACION = prefix("predicciones_generac");
    public static final TableSelector RUTAS = exact("rutas");
    public static final TableSelector RUTAS_CLIENTES = exact("rutas_clientes");
    public static final TableSelector TARIFAS_MERCADO = exact("tarifas_mercado");
    public static final TableSelector V_ESTADO_FLOTA = exact("v_estado_flota");
    public static final TableSelector V_GENERACION_MENSUAL = exact("v_generacion_mensual");
    public static final TableSelector V_ORDENES_PENDIENTES = exact("v_ordenes_pendientes");

    public static final List<TableSelector> ALL = List.of(
            AUDITORIAS,
            CAPEX_ACTIVOS,
            CATALOGO_METODO_TRAT,
            CATALOGO_TIPO_INSTALACIONES,
            CATALOGO_TIPO_RESIDUO,
            CLIENTES_INSTALACIONES,
            CLUSTERS_CLIENTES,
            CLUSTERS_GENERADORES,
            DEPARTAMENTOS,
            DISPOSICION_FINAL,
            EJECUCIONES_RUTA,
            EPP_ENTREGAS,
            FLOTA_TRANSPORTE,
            HISTORICO_GENERACION,
            LOTES_TRATAMIENTO,
            MANIFIESTOS_TRANSPORTE,
            MUNICIPIOS,
            ORDENES_LOTES,
            ORDENES_RECOLECCION,
            PERSONAL,
            PLANTAS_TRATAMIENTO,
            PREDICCIONES_GENERACION,
            RUTAS,
            RUTAS_CLIENTES,
            TARIFAS_MERCADO,
            V_ESTADO_FLOTA,
            V_GENERACION_MENSUAL,
            V_ORDENES_PENDIENTES
    );

    public static TableSelector exact(String tableName) {
        return new TableSelector(tableName, false);
    }

    public static TableSelector prefix(String prefix) {
        return new TableSelector(prefix, true);
    }
}

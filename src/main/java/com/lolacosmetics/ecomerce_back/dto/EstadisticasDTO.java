package com.lolacosmetics.ecomerce_back.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.util.Map;

@Data
public class EstadisticasDTO {
    private Long totalUsuarios;
    private Long totalProductos;
    private Long totalPedidos;
    private Long totalPedidosPendientes;
    private BigDecimal ingresosTotales;
    private BigDecimal ingresosMes;
    private Map<String, Long> pedidosPorEstado;
    private Map<String, Object> ventasPorCategoria;
    private Map<String, BigDecimal> ingresosPorMetodoPago;
}
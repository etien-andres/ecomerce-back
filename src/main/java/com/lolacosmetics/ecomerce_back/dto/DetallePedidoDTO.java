package com.lolacosmetics.ecomerce_back.dto;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class DetallePedidoDTO {
    private Long id;
    private Long pedidoId;
    private Long productoId;
    private String productoNombre;
    private Integer cantidad;
    private BigDecimal precioUnitario;
    private BigDecimal subtotal;
}
package com.lolacosmetics.ecomerce_back.dto;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class CarritoProductoDTO {
    private Long carritoId;
    private Long productoId;
    private String productoNombre;
    private String productoImagen;
    private BigDecimal precioUnitario;
    private Integer cantidad;
    private BigDecimal subtotal;
}
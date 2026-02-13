package com.lolacosmetics.ecomerce_back.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class PedidoDTO {
    private Long id;
    private Long usuarioId;
    private String usuarioNombre;
    private String usuarioEmail;
    private LocalDateTime fechaPedido;
    private String estado;
    private BigDecimal total;
    private List<DetallePedidoDTO> detalles;
    private PagoDTO pago;
}
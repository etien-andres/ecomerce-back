package com.lolacosmetics.ecomerce_back.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class PagoRespuestaDTO {
    private Long id;
    private Long pedidoId;
    private BigDecimal monto;
    private String metodoPago;
    private String estadoPago;
    private String codigoTransaccion;
    private LocalDateTime fechaPago;
    private String ultimosDigitos;
    private String tipoTarjeta;
    private Integer cuotas;
    private String mensaje;
}
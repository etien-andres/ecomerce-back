package com.lolacosmetics.ecomerce_back.dto;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class CarritoDTO {
    private Long id;
    private Long usuarioId;
    private String usuarioEmail;
    private LocalDateTime fechaCreacion;
    private List<CarritoProductoDTO> productos;
    private Integer totalItems;
    private Double totalPrecio;
}

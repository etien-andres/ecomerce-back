package com.lolacosmetics.ecomerce_back.dto;

import com.lolacosmetics.ecomerce_back.model.CarritoProducto;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class CarritoDTO {

    private Long id;
    private Long usuarioId;
    private String usuarioEmail;
    private LocalDateTime fechaCreacion;
    private List<CarritoProducto> productos;
    private Integer totalItems;
    private Double totalPrecio;


    public CarritoDTO(Long id, Long usuarioId, String usuarioEmail, LocalDateTime fechaCreacion, List<CarritoProducto> productos, Integer totalItems) {
        this.id = id;
        this.usuarioId = usuarioId;
        this.usuarioEmail = usuarioEmail;
        this.fechaCreacion = fechaCreacion;
        this.productos = productos;
        this.totalItems = totalItems;

    }
}

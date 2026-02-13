package com.lolacosmetics.ecomerce_back.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class ResenaDTO {
    private Long id;


    private Integer calificacion;

    private String titulo;

    private String comentario;

    private LocalDateTime fecha;

    private String nombreUsuario;

    private String nombreProducto;

    private Boolean verificada;

    private Integer util;
}
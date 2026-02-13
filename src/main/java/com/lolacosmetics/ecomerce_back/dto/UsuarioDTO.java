package com.lolacosmetics.ecomerce_back.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class UsuarioDTO {
    private Long id;


    private String nombre;


    private String email;


    private String password;

    private String direccion;

    private String telefono;

    private LocalDateTime fechaRegistro;
}
package com.lolacosmetics.ecomerce_back.dto;

import lombok.Data;
import jakarta.validation.constraints.*;

@Data
public class LoginDTO {

    @NotBlank(message = "El email es obligatorio")
    @Email(message = "Formato de email inválido")
    private String email;

    @NotBlank(message = "La contraseña es obligatoria")
    private String password;
}
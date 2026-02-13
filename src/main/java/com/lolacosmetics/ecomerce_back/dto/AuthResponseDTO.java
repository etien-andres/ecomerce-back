package com.lolacosmetics.ecomerce_back.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthResponseDTO {
    private String token;
    private String tipo = "Bearer";
    private Long id;
    private String email;
    private String nombre;
    private String refreshToken;
    private String rol;
    private Long expiraEn;

    public AuthResponseDTO(String token, Long id, String email, String nombre) {
        this.token = token;
        this.id = id;
        this.email = email;
        this.nombre = nombre;
    }
}
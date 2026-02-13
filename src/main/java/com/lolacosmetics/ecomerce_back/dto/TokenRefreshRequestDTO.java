package com.lolacosmetics.ecomerce_back.dto;

import lombok.Data;
import jakarta.validation.constraints.NotBlank;

@Data
public class TokenRefreshRequestDTO {
    @NotBlank(message = "El refresh token es obligatorio")
    private String refreshToken;
}
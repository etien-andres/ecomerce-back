package com.lolacosmetics.ecomerce_back.dto;
import lombok.Data;

@Data
public class LoginRequestDTO {
    private String email;
    private String password;
}
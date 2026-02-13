package com.lolacosmetics.ecomerce_back.dto;
import lombok.Data;
import java.util.List;

@Data
public class PedidoRequestDTO {
    private String email;
    private List<CarritoItemDTO> items;
}


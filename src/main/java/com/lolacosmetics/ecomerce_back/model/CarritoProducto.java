package com.lolacosmetics.ecomerce_back.model;


import jakarta.persistence.*;
import lombok.Data;
import java.io.Serializable;

@Entity
@Data
@Table(name = "carrito_producto")
@IdClass(CarritoProductoId.class)
public class CarritoProducto {
    @Id
    @ManyToOne
    @JoinColumn(name = "id_carrito")
    private Carrito carrito;

    @Id
    @ManyToOne
    @JoinColumn(name = "id_producto")
    private Producto producto;

    @Column(nullable = false)
    private Integer cantidad;
}

// Clase ID compuesta
class CarritoProductoId implements Serializable {
    private Long carrito;
    private Long producto;
}
package com.lolacosmetics.ecomerce_back.model;

import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;
import java.util.List;

@Entity
@Data
@Table(name = "producto")
public class Producto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nombre;

    @Column(nullable = false)
    private String descripcion;

    @Column()
    private String img;

    @Column(nullable = false)
    private BigDecimal precio;

    @Column()
    private Integer stock;

    @ManyToOne
    @JoinColumn(name = "id_categoria")
    private Categoria categoria;

    @OneToMany(mappedBy = "producto")
    private List<DetallePedido> detallesPedido;

    @OneToMany(mappedBy = "producto")
    private List<Resena> resenas;
}
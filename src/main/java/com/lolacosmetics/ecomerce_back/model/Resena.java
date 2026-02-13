package com.lolacosmetics.ecomerce_back.model;


import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "resena",
        uniqueConstraints = @UniqueConstraint(columnNames = {"id_usuario", "id_producto"}))
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Resena {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_usuario", nullable = false)
    @ToString.Exclude
    @JsonIgnoreProperties({"resenas", "pedidos", "carrito", "password"})
    private Usuario usuario;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_producto", nullable = false)
    @ToString.Exclude
    @JsonIgnoreProperties({"resenas", "detallesPedido", "categoria"})
    private Producto producto;

    @Column(name = "calificacion", nullable = false)
    private Integer calificacion; // 1-5 estrellas

    @Column(name = "comentario", length = 1000)
    private String comentario;

    @Column(name = "titulo", length = 200)
    private String titulo;

    @Column(name = "fecha")
    private LocalDateTime fecha = LocalDateTime.now();


    @Column(name = "verificada")
    private Boolean verificada = false; // Reseña de compra verificada

    @Column(name = "util")
    private Integer util = 0; // Votos de útil

    @Column(name = "no_util")
    private Integer noUtil = 0; // Votos de no útil

    // Constructor vacío
    public Resena() {}

    // Constructor con campos principales
    public Resena(Usuario usuario, Producto producto, Integer calificacion, String comentario) {
        this.usuario = usuario;
        this.producto = producto;
        this.calificacion = calificacion;
        this.comentario = comentario;
    }

    // Método para validar calificación
    public void setCalificacion(Integer calificacion) {
        if (calificacion == null || calificacion < 1 || calificacion > 5) {
            throw new IllegalArgumentException("La calificación debe estar entre 1 y 5");
        }
        this.calificacion = calificacion;
    }
}
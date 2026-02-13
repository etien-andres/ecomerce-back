package com.lolacosmetics.ecomerce_back.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

import java.util.List;

@Entity
@Data
@Table(name = "categoria")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Categoria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nombre", nullable = false, unique = true, length = 100)
    private String nombre;

    @Column(name = "descripcion", length = 500)
    private String descripcion;

    @Column(name = "activa")
    private Boolean activa = true;

    @Column(name = "fecha_creacion")
    private java.time.LocalDateTime fechaCreacion = java.time.LocalDateTime.now();

    @Column(name = "imagen_url")
    private String imagenUrl;

    // Relación OneToMany con Producto
    @OneToMany(mappedBy = "categoria", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @ToString.Exclude
    @JsonIgnoreProperties("categoria")
    private List<Producto> productos;

    // Constructor vacío
    public Categoria() {}

    // Constructor con campos principales
    public Categoria(String nombre, String descripcion) {
        this.nombre = nombre;
        this.descripcion = descripcion;
    }

    // Métodos helpers
    public void agregarProducto(Producto producto) {
        productos.add(producto);
        producto.setCategoria(this);
    }

    public void removerProducto(Producto producto) {
        productos.remove(producto);
        producto.setCategoria(null);
    }
}
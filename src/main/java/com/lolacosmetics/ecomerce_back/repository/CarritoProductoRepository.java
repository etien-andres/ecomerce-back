package com.lolacosmetics.ecomerce_back.repository;


import com.lolacosmetics.ecomerce_back.model.Carrito;
import com.lolacosmetics.ecomerce_back.model.CarritoProducto;
import com.lolacosmetics.ecomerce_back.model.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface CarritoProductoRepository extends JpaRepository<CarritoProducto, Long> {
    List<CarritoProducto> findByCarrito(Carrito carrito);
    Optional<CarritoProducto> findByCarritoAndProducto(Carrito carrito, Producto producto);
    void deleteByCarrito(Carrito carrito);
}
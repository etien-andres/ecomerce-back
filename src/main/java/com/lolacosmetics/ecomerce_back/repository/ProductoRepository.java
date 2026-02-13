package com.lolacosmetics.ecomerce_back.repository;

import com.lolacosmetics.ecomerce_back.model.Producto;
import com.lolacosmetics.ecomerce_back.model.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ProductoRepository extends JpaRepository<Producto, Long> {
    List<Producto> findByCategoria(Categoria categoria);
    List<Producto> findByStockLessThan(Integer stock);
}
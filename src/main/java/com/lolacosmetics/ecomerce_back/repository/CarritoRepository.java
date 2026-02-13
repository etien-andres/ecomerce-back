package com.lolacosmetics.ecomerce_back.repository;

import com.lolacosmetics.ecomerce_back.model.Carrito;
import com.lolacosmetics.ecomerce_back.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface CarritoRepository extends JpaRepository<Carrito, Long> {
    Optional<Carrito> findByUsuario(Usuario usuario);
    void deleteByUsuario(Usuario usuario);
}
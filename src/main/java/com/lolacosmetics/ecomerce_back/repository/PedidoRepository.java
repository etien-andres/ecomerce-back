package com.lolacosmetics.ecomerce_back.repository;

import com.lolacosmetics.ecomerce_back.model.Pedido;
import com.lolacosmetics.ecomerce_back.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface PedidoRepository extends JpaRepository<Pedido, Long> {
    List<Pedido> findByUsuario(Usuario usuario);
    List<Pedido> findByEstado(String estado);
}

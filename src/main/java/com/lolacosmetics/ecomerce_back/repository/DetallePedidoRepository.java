package com.lolacosmetics.ecomerce_back.repository;

import com.lolacosmetics.ecomerce_back.model.DetallePedido;
import com.lolacosmetics.ecomerce_back.model.Pedido;
import com.lolacosmetics.ecomerce_back.model.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface DetallePedidoRepository extends JpaRepository<DetallePedido, Long> {
    List<DetallePedido> findByPedido(Pedido pedido);
    List<DetallePedido> findByProducto(Producto producto);
}
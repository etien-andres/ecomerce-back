package com.lolacosmetics.ecomerce_back.service;

import com.lolacosmetics.ecomerce_back.dto.CarritoItemDTO;
import com.lolacosmetics.ecomerce_back.model.*;
import com.lolacosmetics.ecomerce_back.dto.PedidoRequestDTO;
import com.lolacosmetics.ecomerce_back.repository.PedidoRepository;
import com.lolacosmetics.ecomerce_back.repository.UsuarioRepository;
import com.lolacosmetics.ecomerce_back.repository.ProductoRepository;
import com.lolacosmetics.ecomerce_back.repository.DetallePedidoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class PedidoService {

    @Autowired
    private PedidoRepository pedidoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private DetallePedidoRepository detallePedidoRepository;

    @Autowired
    private ProductoService productoService;

    @Transactional
    public Pedido crear(PedidoRequestDTO request) {
        Usuario usuario = usuarioRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        Pedido pedido = new Pedido();
        pedido.setUsuario(usuario);
        pedido.setFechaPedido(LocalDateTime.now());
        pedido.setEstado(EstadoPedido.PENDIENTE);

        BigDecimal total = BigDecimal.ZERO;
        List<CarritoItemDTO> items= request.getItems();
        for (CarritoItemDTO item : request.getItems()) {
            Producto producto = productoRepository.findById(item.getProductoId())
                    .orElseThrow(() -> new RuntimeException("Producto no encontrado: " + item.getProductoId()));

            // Validar stock
            if (producto.getStock() < item.getCantidad()) {
                throw new RuntimeException("Stock insuficiente para: " + producto.getNombre());
            }

            DetallePedido detalle = new DetallePedido();
            detalle.setPedido(pedido);
            detalle.setProducto(producto);
            detalle.setCantidad(item.getCantidad());
            detalle.setPrecioUnitario(producto.getPrecio());

            pedido.getDetalles().add(detalle);

            BigDecimal subtotal = producto.getPrecio()
                    .multiply(BigDecimal.valueOf(item.getCantidad()));
            total = total.add(subtotal);
        }

        pedido.setTotal(total);
        Pedido pedidoGuardado = pedidoRepository.save(pedido);

        // Actualizar stock
        for (DetallePedido detalle : pedido.getDetalles()) {
            productoService.actualizarStock(detalle.getProducto().getId(), detalle.getCantidad());
        }

        return pedidoGuardado;
    }

    @Transactional(readOnly = true)
    public Pedido obtenerPorId(Long id) {
        return pedidoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pedido no encontrado"));
    }

    @Transactional(readOnly = true)
    public List<Pedido> obtenerPorUsuario(Long usuarioId) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        return pedidoRepository.findByUsuario(usuario);
    }

    @Transactional(readOnly = true)
    public List<Pedido> obtenerPorEstado(EstadoPedido estado) {
        return pedidoRepository.findByEstado(estado.toString());
    }

    @Transactional
    public Pedido actualizarEstado(Long id, EstadoPedido nuevoEstado) {
        Pedido pedido = obtenerPorId(id);
        pedido.setEstado(nuevoEstado);
        return pedidoRepository.save(pedido);
    }
}
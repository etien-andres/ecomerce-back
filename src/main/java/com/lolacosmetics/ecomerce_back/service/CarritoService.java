package com.lolacosmetics.ecomerce_back.service;

import com.lolacosmetics.ecomerce_back.model.*;
import com.lolacosmetics.ecomerce_back.dto.CarritoProductoDTO;
import com.lolacosmetics.ecomerce_back.repository.CarritoRepository;
import com.lolacosmetics.ecomerce_back.repository.CarritoProductoRepository;
import com.lolacosmetics.ecomerce_back.repository.UsuarioRepository;
import com.lolacosmetics.ecomerce_back.repository.ProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.util.List;

@Service
public class CarritoService {

    @Autowired
    private CarritoRepository carritoRepository;

    @Autowired
    private CarritoProductoRepository carritoProductoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private ProductoRepository productoRepository;

    @Transactional(readOnly = true)
    public Carrito obtenerPorUsuario(Long usuarioId) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        carritoProductoRepository.findByCarrito(carritoRepository.findByUsuario(usuario).orElseThrow(() -> new RuntimeException("Carrito no encontrado")));
        return carritoRepository.findByUsuario(usuario)
                .orElseGet(() -> {
                    Carrito nuevoCarrito = new Carrito();
                    nuevoCarrito.setUsuario(usuario);
                    return carritoRepository.save(nuevoCarrito);
                });
    }


    @Transactional
    public List<CarritoProducto> obtenerListadeProd(Long usuarioId) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        List<CarritoProducto> carrito = carritoProductoRepository.findByCarrito(carritoRepository.findByUsuario(usuario).orElseThrow(() -> new RuntimeException("Carrito no encontrado")));

        return carrito;
    }

    @Transactional
    public CarritoProducto agregarProducto(Long usuarioId, CarritoProductoDTO dto) {
        Carrito carrito = obtenerPorUsuario(usuarioId);
        Producto producto = productoRepository.findById(dto.getProductoId())
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

        // Validar stock
        if (producto.getStock() < dto.getCantidad()) {
            throw new RuntimeException("Stock insuficiente. Disponible: " + producto.getStock());
        }

        // Buscar si ya existe el producto en el carrito
        CarritoProducto carritoProducto = carritoProductoRepository
                .findByCarritoAndProducto(carrito, producto)
                .orElse(new CarritoProducto());

        carritoProducto.setCarrito(carrito);
        carritoProducto.setProducto(producto);
        carritoProducto.setCantidad(dto.getCantidad());

        return carritoProductoRepository.save(carritoProducto);
    }

    @Transactional
    public void actualizarCantidad(Long usuarioId, Long productoId, Integer cantidad) {
        Carrito carrito = obtenerPorUsuario(usuarioId);
        Producto producto = productoRepository.findById(productoId)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

        CarritoProducto item = carritoProductoRepository
                .findByCarritoAndProducto(carrito, producto)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado en el carrito"));

        if (cantidad <= 0) {
            carritoProductoRepository.delete(item);
        } else {
            item.setCantidad(cantidad);
            carritoProductoRepository.save(item);
        }
    }

    @Transactional
    public void eliminarProducto(Long usuarioId, Long productoId) {
        Carrito carrito = obtenerPorUsuario(usuarioId);
        Producto producto = productoRepository.findById(productoId)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

        carritoProductoRepository.findByCarritoAndProducto(carrito, producto)
                .ifPresent(carritoProductoRepository::delete);
    }

    @Transactional
    public void limpiarCarrito(Long usuarioId) {
        Carrito carrito = obtenerPorUsuario(usuarioId);
        carritoProductoRepository.deleteByCarrito(carrito);
    }

    @Transactional(readOnly = true)
    public BigDecimal calcularTotal(Long usuarioId) {
        Carrito carrito = obtenerPorUsuario(usuarioId);
        List<CarritoProducto> items = carritoProductoRepository.findByCarrito(carrito);

        return items.stream()
                .map(item -> item.getProducto().getPrecio()
                        .multiply(BigDecimal.valueOf(item.getCantidad())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
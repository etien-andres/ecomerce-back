package com.lolacosmetics.ecomerce_back.controller;

import com.lolacosmetics.ecomerce_back.dto.CarritoDTO;
import com.lolacosmetics.ecomerce_back.model.Carrito;
import com.lolacosmetics.ecomerce_back.model.CarritoProducto;
import com.lolacosmetics.ecomerce_back.dto.CarritoProductoDTO;
import com.lolacosmetics.ecomerce_back.service.CarritoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/carrito")
@CrossOrigin(origins = "*")
public class CarritoController {

    @Autowired
    private CarritoService carritoService;

    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<?> obtenerCarrito(@PathVariable Long usuarioId) {
        try {
            Carrito carrito = carritoService.obtenerPorUsuario(usuarioId);
            CarritoProductoDTO carritoProductoDTO =new CarritoProductoDTO();

            CarritoDTO carritoDto=new CarritoDTO(carrito.getId(), carrito.getUsuario().getId(),carrito.getUsuario().getEmail(),carrito.getFechaCreacion(),carrito.getProductos(),carrito.getProductos().size());
            carritoDto.setFechaCreacion(carrito.getFechaCreacion());


            return ResponseEntity.ok(carritoDto);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/usuario/{usuarioId}/agregar")
    public ResponseEntity<?> agregarProducto(
            @PathVariable Long usuarioId,
            @Valid @RequestBody CarritoProductoDTO dto) {
        try {
            CarritoProducto item = carritoService.agregarProducto(usuarioId, dto);
            return ResponseEntity.status(HttpStatus.CREATED).body(item);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/usuario/{usuarioId}/producto/{productoId}")
    public ResponseEntity<?> actualizarCantidad(
            @PathVariable Long usuarioId,
            @PathVariable Long productoId,
            @RequestParam Integer cantidad) {
        try {
            carritoService.actualizarCantidad(usuarioId, productoId, cantidad);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/usuario/{usuarioId}/producto/{productoId}")
    public ResponseEntity<?> eliminarProducto(
            @PathVariable Long usuarioId,
            @PathVariable Long productoId) {
        try {
            carritoService.eliminarProducto(usuarioId, productoId);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/usuario/{usuarioId}/limpiar")
    public ResponseEntity<?> limpiarCarrito(@PathVariable Long usuarioId) {
        try {
            carritoService.limpiarCarrito(usuarioId);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }



}
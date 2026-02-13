package com.lolacosmetics.ecomerce_back.controller;

import com.lolacosmetics.ecomerce_back.model.Producto;
import com.lolacosmetics.ecomerce_back.dto.ProductoDTO;
import com.lolacosmetics.ecomerce_back.service.ProductoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/productos")
@CrossOrigin(origins = "*")
public class ProductoController {

    @Autowired
    private ProductoService productoService;

    @GetMapping
    public ResponseEntity<List<Producto>> listarTodos() {
        return ResponseEntity.ok(productoService.listarProductos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerPorId(@PathVariable Long id) {
        try {
            Producto producto = productoService.obtenerProducto(id);
            return ResponseEntity.ok(producto);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
//@TODO
//    @GetMapping("/buscar")
//    public ResponseEntity<List<Producto>> buscarPorNombre(@RequestParam String nombre) {
//        return ResponseEntity.ok(productoService.buscarPorNombre(nombre));
//    }

    @PostMapping
    public ResponseEntity<?> crear(@Valid @RequestBody ProductoDTO productop) {
        try {
            Producto producto = productoService.crearProducto(productop);
            return ResponseEntity.status(HttpStatus.CREATED).body(producto);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> actualizar(@PathVariable Long id, @Valid @RequestBody ProductoDTO productoDTO) {
        try {
            Producto producto = productoService.actualizar(id, productoDTO);
            return ResponseEntity.ok(producto);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(@PathVariable Long id) {
        try {
            productoService.eliminar(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
package com.lolacosmetics.ecomerce_back.controller;

import com.lolacosmetics.ecomerce_back.model.Resena;
import com.lolacosmetics.ecomerce_back.service.ResenaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class ResenaController {

    @Autowired
    private ResenaService resenaService;

    @GetMapping("/productos/{productoId}/resenas")
    public ResponseEntity<List<Resena>> obtenerResenasPorProducto(@PathVariable Long productoId) {
        return ResponseEntity.ok(resenaService.obtenerResenasPorProducto(productoId));
    }

    @GetMapping("/usuarios/{usuarioId}/resenas")
    public ResponseEntity<List<Resena>> obtenerResenasPorUsuario(@PathVariable Long usuarioId) {
        return ResponseEntity.ok(resenaService.obtenerResenasPorUsuario(usuarioId));
    }

    @GetMapping("/productos/{productoId}/resenas/promedio")
    public ResponseEntity<Double> obtenerPromedioCalificacion(@PathVariable Long productoId) {
        return ResponseEntity.ok(resenaService.obtenerPromedioCalificacion(productoId));
    }

    @GetMapping("/productos/{productoId}/resenas/top")
    public ResponseEntity<List<Resena>> obtenerTopResenas(@PathVariable Long productoId) {
        return ResponseEntity.ok(resenaService.obtenerTopResenas(productoId));
    }

    @PostMapping("/usuarios/{usuarioId}/productos/{productoId}/resenas")
    public ResponseEntity<Resena> crearResena(
            @PathVariable Long usuarioId,
            @PathVariable Long productoId,
            @Valid @RequestBody Resena resena) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(resenaService.crearResena(usuarioId, productoId, resena));
    }

    @PutMapping("/resenas/{resenaId}/usuarios/{usuarioId}")
    public ResponseEntity<Resena> actualizarResena(
            @PathVariable Long resenaId,
            @PathVariable Long usuarioId,
            @Valid @RequestBody Resena resena) {
        return ResponseEntity.ok(resenaService.actualizarResena(resenaId, usuarioId, resena));
    }

    @DeleteMapping("/resenas/{resenaId}/usuarios/{usuarioId}")
    public ResponseEntity<Void> eliminarResena(
            @PathVariable Long resenaId,
            @PathVariable Long usuarioId) {
        resenaService.eliminarResena(resenaId, usuarioId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/resenas/{resenaId}/votar")
    public ResponseEntity<Void> votarUtil(
            @PathVariable Long resenaId,
            @RequestParam boolean util) {
        resenaService.votarUtil(resenaId, util);
        return ResponseEntity.ok().build();
    }
}
package com.lolacosmetics.ecomerce_back.service;

import com.lolacosmetics.ecomerce_back.model.Resena;
import com.lolacosmetics.ecomerce_back.model.Producto;
import com.lolacosmetics.ecomerce_back.model.Usuario;
import com.lolacosmetics.ecomerce_back.repository.ResenaRepository;
import com.lolacosmetics.ecomerce_back.repository.ProductoRepository;
import com.lolacosmetics.ecomerce_back.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class ResenaService {

    @Autowired
    private ResenaRepository resenaRepository;

    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Transactional(readOnly = true)
    public List<Resena> obtenerResenasPorProducto(Long productoId) {
        Producto producto = productoRepository.findById(productoId)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));
        return resenaRepository.findByProductoOrderByFechaDesc(producto);
    }

    @Transactional(readOnly = true)
    public List<Resena> obtenerResenasPorUsuario(Long usuarioId) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        return resenaRepository.findByUsuarioOrderByFechaDesc(usuario);
    }

    @Transactional
    public Resena crearResena(Long usuarioId, Long productoId, Resena resena) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        Producto producto = productoRepository.findById(productoId)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

        // Verificar si ya existe una reseña de este usuario para este producto
        if (resenaRepository.findByUsuarioAndProducto(usuario, producto).isPresent()) {
            throw new RuntimeException("Ya has realizado una reseña para este producto");
        }

        resena.setUsuario(usuario);
        resena.setProducto(producto);

        // Verificar si el usuario compró el producto (opcional)
        boolean haComprado = usuario.getPedidos().stream()
                .flatMap(pedido -> pedido.getDetalles().stream())
                .anyMatch(detalle -> detalle.getProducto().getId().equals(productoId));

        resena.setVerificada(haComprado);

        return resenaRepository.save(resena);
    }

    @Transactional
    public Resena actualizarResena(Long resenaId, Long usuarioId, Resena resenaActualizada) {
        Resena resena = resenaRepository.findById(resenaId)
                .orElseThrow(() -> new RuntimeException("Reseña no encontrada"));

        // Verificar que el usuario sea el dueño de la reseña
        if (!resena.getUsuario().getId().equals(usuarioId)) {
            throw new RuntimeException("No tienes permiso para editar esta reseña");
        }

        resena.setTitulo(resenaActualizada.getTitulo());
        resena.setComentario(resenaActualizada.getComentario());
        resena.setCalificacion(resenaActualizada.getCalificacion());

        return resenaRepository.save(resena);
    }

    @Transactional
    public void eliminarResena(Long resenaId, Long usuarioId) {
        Resena resena = resenaRepository.findById(resenaId)
                .orElseThrow(() -> new RuntimeException("Reseña no encontrada"));

        // Verificar que el usuario sea el dueño de la reseña o admin
        if (!resena.getUsuario().getId().equals(usuarioId)) {
            throw new RuntimeException("No tienes permiso para eliminar esta reseña");
        }

        resenaRepository.delete(resena);
    }

    @Transactional(readOnly = true)
    public Double obtenerPromedioCalificacion(Long productoId) {
        Double promedio = resenaRepository.promedioCalificacionesByProducto(productoId);
        return promedio != null ? Math.round(promedio * 10) / 10.0 : 0.0;
    }

    @Transactional
    public void votarUtil(Long resenaId, boolean util) {
        Resena resena = resenaRepository.findById(resenaId)
                .orElseThrow(() -> new RuntimeException("Reseña no encontrada"));

        if (util) {
            resena.setUtil(resena.getUtil() + 1);
        } else {
            resena.setNoUtil(resena.getNoUtil() + 1);
        }

        resenaRepository.save(resena);
    }

    @Transactional(readOnly = true)
    public List<Resena> obtenerTopResenas(Long productoId) {
        return resenaRepository.findTop3ByProductoOrderByUtilDesc(
                productoId,
                PageRequest.of(0, 3)
        );
    }
}
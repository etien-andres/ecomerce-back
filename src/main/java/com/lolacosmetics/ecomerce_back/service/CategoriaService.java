package com.lolacosmetics.ecomerce_back.service;


import com.lolacosmetics.ecomerce_back.model.Categoria;
import com.lolacosmetics.ecomerce_back.repository.CategoriaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class CategoriaService {

    @Autowired
    private CategoriaRepository categoriaRepository;

    @Transactional(readOnly = true)
    public List<Categoria> listarTodas() {
        return categoriaRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<Categoria> listarActivas() {
        return categoriaRepository.findByActivaTrue();
    }

    @Transactional(readOnly = true)
    public Categoria obtenerPorId(Long id) {
        return categoriaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada con ID: " + id));
    }

    @Transactional(readOnly = true)
    public Categoria obtenerPorNombre(String nombre) {
        return categoriaRepository.findByNombre(nombre)
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada: " + nombre));
    }

    @Transactional
    public Categoria crearCategoria(Categoria categoria) {
        if (categoriaRepository.existsByNombre(categoria.getNombre())) {
            throw new RuntimeException("Ya existe una categoría con el nombre: " + categoria.getNombre());
        }
        return categoriaRepository.save(categoria);
    }

    @Transactional
    public Categoria actualizarCategoria(Long id, Categoria categoriaActualizada) {
        Categoria categoria = obtenerPorId(id);

        // Validar si el nombre nuevo ya existe
        if (!categoria.getNombre().equals(categoriaActualizada.getNombre()) &&
                categoriaRepository.existsByNombre(categoriaActualizada.getNombre())) {
            throw new RuntimeException("Ya existe una categoría con el nombre: " + categoriaActualizada.getNombre());
        }

        categoria.setNombre(categoriaActualizada.getNombre());
        categoria.setDescripcion(categoriaActualizada.getDescripcion());
        categoria.setActiva(categoriaActualizada.getActiva());
        categoria.setImagenUrl(categoriaActualizada.getImagenUrl());

        return categoriaRepository.save(categoria);
    }

    @Transactional
    public void eliminarCategoria(Long id) {
        Categoria categoria = obtenerPorId(id);

        // Verificar si tiene productos asociados
        if (categoria.getProductos() != null && !categoria.getProductos().isEmpty()) {
            // Opción 1: Lanzar excepción
            throw new RuntimeException("No se puede eliminar la categoría porque tiene productos asociados");

            // Opción 2: Desactivar en lugar de eliminar
            // categoria.setActiva(false);
            // categoriaRepository.save(categoria);
        } else {
            categoriaRepository.delete(categoria);
        }
    }

    @Transactional
    public void desactivarCategoria(Long id) {
        Categoria categoria = obtenerPorId(id);
        categoria.setActiva(false);
        categoriaRepository.save(categoria);
    }

    @Transactional(readOnly = true)
    public List<Categoria> buscarPorNombre(String nombre) {
        return categoriaRepository.findByNombreContainingIgnoreCase(nombre);
    }
}
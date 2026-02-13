package com.lolacosmetics.ecomerce_back.repository;


import com.lolacosmetics.ecomerce_back.model.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface CategoriaRepository extends JpaRepository<Categoria, Long> {

    // Buscar por nombre exacto
    Optional<Categoria> findByNombre(String nombre);

    // Buscar categorías activas
    List<Categoria> findByActivaTrue();

    // Buscar por nombre que contenga (ignorando mayúsculas)
    List<Categoria> findByNombreContainingIgnoreCase(String nombre);

    // Contar productos por categoría
    @Query("SELECT c, COUNT(p) FROM Categoria c LEFT JOIN c.productos p GROUP BY c")
    List<Object[]> countProductosByCategoria();

    // Buscar categorías con stock de productos > 0
    @Query("SELECT DISTINCT c FROM Categoria c JOIN c.productos p WHERE p.stock > 0")
    List<Categoria> findCategoriasConProductosDisponibles();

    // Verificar si existe por nombre
    boolean existsByNombre(String nombre);
}
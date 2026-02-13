package com.lolacosmetics.ecomerce_back.repository;


import com.lolacosmetics.ecomerce_back.model.Resena;
import com.lolacosmetics.ecomerce_back.model.Producto;
import com.lolacosmetics.ecomerce_back.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface ResenaRepository extends JpaRepository<Resena, Long> {

    // Buscar reseñas por producto
    List<Resena> findByProductoOrderByFechaDesc(Producto producto);

    // Buscar reseñas por usuario
    List<Resena> findByUsuarioOrderByFechaDesc(Usuario usuario);

    // Buscar reseña específica de usuario para un producto
    Optional<Resena> findByUsuarioAndProducto(Usuario usuario, Producto producto);

    // Calcular promedio de calificaciones por producto
    @Query("SELECT AVG(r.calificacion) FROM Resena r WHERE r.producto.id = :productoId")
    Double promedioCalificacionesByProducto(@Param("productoId") Long productoId);

    // Contar reseñas por calificación
    @Query("SELECT r.calificacion, COUNT(r) FROM Resena r WHERE r.producto.id = :productoId GROUP BY r.calificacion")
    List<Object[]> countByCalificacion(@Param("productoId") Long productoId);

    // Buscar reseñas verificadas (compradores reales)
    List<Resena> findByProductoAndVerificadaTrue(Producto producto);

    // Top 3 reseñas más útiles
    @Query("SELECT r FROM Resena r WHERE r.producto.id = :productoId ORDER BY r.util DESC")
    List<Resena> findTop3ByProductoOrderByUtilDesc(@Param("productoId") Long productoId,
                                                   org.springframework.data.domain.Pageable pageable);

    // Eliminar reseñas de un usuario específico
    void deleteByUsuario(Usuario usuario);
}
package com.lolacosmetics.ecomerce_back.repository;

import aj.org.objectweb.asm.commons.Remapper;
import com.lolacosmetics.ecomerce_back.model.Pago;
import com.lolacosmetics.ecomerce_back.model.Pedido;
import com.lolacosmetics.ecomerce_back.model.EstadoPago;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface PagoRepository extends JpaRepository<Pago, Long> {

    // Buscar por pedido
    Optional<Pago> findByPedido(Pedido pedido);



    // Listar pagos por estado
    List<Pago> findByEstadoPago(EstadoPago estadoPago);

    // Listar pagos por método
    List<Pago> findByMetodoPago(String metodoPago);

    // Pagos completados en un rango de fechas
    List<Pago> findByEstadoPagoAndFechaPagoBetween(
            EstadoPago estadoPago,
            LocalDateTime inicio,
            LocalDateTime fin
    );

    // Estadísticas de pagos por método
    @Query("SELECT p.metodoPago, COUNT(p), SUM(p.monto) FROM Pago p " +
            "WHERE p.estadoPago = 'COMPLETADO' " +
            "GROUP BY p.metodoPago")
    List<Object[]> estadisticasPorMetodo();

    // Total de ingresos en un período
    @Query("SELECT COALESCE(SUM(p.monto), 0) FROM Pago p " +
            "WHERE p.estadoPago = 'COMPLETADO' " +
            "AND p.fechaPago BETWEEN :inicio AND :fin")
    BigDecimal totalIngresosPeriodo(
            @Param("inicio") LocalDateTime inicio,
            @Param("fin") LocalDateTime fin
    );


    Optional<Pago> findByPedidoId(Long pedidoId);
}

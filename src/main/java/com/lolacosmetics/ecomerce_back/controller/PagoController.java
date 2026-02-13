package com.lolacosmetics.ecomerce_back.controller;

import com.lolacosmetics.ecomerce_back.model.Pago;
import com.lolacosmetics.ecomerce_back.dto.PagoDTO;
import com.lolacosmetics.ecomerce_back.service.PagoService;
import com.lolacosmetics.ecomerce_back.repository.PagoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/pagos")
@CrossOrigin(origins = "*")
public class PagoController {

    @Autowired
    private PagoService pagoService;

    @Autowired
    private PagoRepository pagoRepository;

    @PostMapping("/procesar")
    public ResponseEntity<?> procesarPago(@Valid @RequestBody PagoDTO pagoDTO) {
        try {
            Pago pago = pagoService.procesarPago(pagoDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(pago);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                    "error", e.getMessage(),
                    "timestamp", LocalDateTime.now()
            ));
        }
    }

    @GetMapping("/pedido/{pedidoId}")
    public ResponseEntity<?> obtenerPagoPorPedido(@PathVariable Long pedidoId) {
        return pagoRepository.findByPedidoId(pedidoId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Pago> obtenerPago(@PathVariable Long id) {
        return pagoRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

//    @GetMapping("/transaccion/{codigo}")
//    public ResponseEntity<Pago> obtenerPorCodigoTransaccion(@PathVariable String codigo) {
//        return pagoRepository.findByCodigoTransaccion(codigo)
//                .map(ResponseEntity::ok)
//                .orElse(ResponseEntity.notFound().build());
//    }

    @PostMapping("/{id}/confirmar-transferencia")
    public ResponseEntity<?> confirmarTransferencia(
            @PathVariable Long id,
            @RequestParam String numeroTransferencia) {
        try {
            Pago pago = pagoService.confirmarTransferencia(id, numeroTransferencia);
            return ResponseEntity.ok(pago);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

//    @PostMapping("/{id}/reembolsar")
//    public ResponseEntity<?> reembolsarPago(@PathVariable Long id) {
//        try {
//            //Pago pago = pagoService.reembolsarPago(id);
//            return ResponseEntity.ok(pago);
//        } catch (Exception e) {
//            return ResponseEntity.badRequest().body(e.getMessage());
//        }
//    }

    @GetMapping("/estadisticas/ingresos")
    public ResponseEntity<?> obtenerIngresos(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime inicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fin) {
        BigDecimal total = pagoRepository.totalIngresosPeriodo(inicio, fin);
        return ResponseEntity.ok(Map.of(
                "periodo_inicio", inicio,
                "periodo_fin", fin,
                "total_ingresos", total
        ));
    }

    @GetMapping("/estadisticas/metodos")
    public ResponseEntity<List<Object[]>> estadisticasPorMetodo() {
        return ResponseEntity.ok(pagoRepository.estadisticasPorMetodo());
    }

    @GetMapping("/webhook/stripe")
    public ResponseEntity<String> webhookStripe(@RequestBody String payload) {
        // Webhook para notificaciones de Stripe
        return ResponseEntity.ok("OK");
    }
}
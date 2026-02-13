package com.lolacosmetics.ecomerce_back.service;

import com.lolacosmetics.ecomerce_back.model.*;
import com.lolacosmetics.ecomerce_back.dto.PagoDTO;
import com.lolacosmetics.ecomerce_back.repository.PagoRepository;
import com.lolacosmetics.ecomerce_back.repository.PedidoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class PagoService {

    @Autowired
    private PagoRepository pagoRepository;

    @Autowired
    private PedidoRepository pedidoRepository;



    @Transactional
    public Pago procesarPago(PagoDTO pagoDTO) {
        // 1. Obtener el pedido
        Pedido pedido = pedidoRepository.findById(pagoDTO.getPedidoId())
                .orElseThrow(() -> new RuntimeException("Pedido no encontrado"));

        // 2. Validar que el pedido no tenga ya un pago
        if (pagoRepository.findByPedido(pedido).isPresent()) {
            throw new RuntimeException("Este pedido ya tiene un pago asociado");
        }

        // 3. Validar monto
        if (pedido.getTotal().compareTo(pagoDTO.getMonto()) != 0) {
            throw new RuntimeException("El monto del pago no coincide con el total del pedido");
        }

        // 4. Crear registro de pago
        Pago pago = new Pago();
        pago.setPedido(pedido);
        pago.setMonto(pagoDTO.getMonto());
        pago.setMetodoPago(MetodoPago.valueOf(pagoDTO.getMetodoPago()));
        pago.setEstadoPago(EstadoPago.PROCESANDO);



        // 5. Procesar según método de pago
        try {
            switch (pago.getMetodoPago()) {
                case TARJETA_CREDITO:
                case TARJETA_DEBITO:
                    procesarPagoTarjeta(pago, pagoDTO);
                    break;
                case PAYPAL:
                    procesarPagoPayPal(pago, pagoDTO);
                    break;
                case WEBPAY:
                    procesarPagoWebpay(pago, pagoDTO);
                    break;
                case TRANSFERENCIA_BANCARIA:
                    procesarTransferencia(pago, pagoDTO);
                    break;
                default:
                    throw new RuntimeException("Método de pago no soportado");
            }
        } catch (Exception e) {
            pago.setEstadoPago(EstadoPago.FALLIDO);
            System.out.println(("Error: " + e.getMessage()));
            pagoRepository.save(pago);
            throw new RuntimeException("Error al procesar el pago: " + e.getMessage());
        }

        // 6. Guardar pago
        Pago pagoGuardado = pagoRepository.save(pago);

        // 7. Actualizar estado del pedido
        if (pagoGuardado.getEstadoPago() == EstadoPago.COMPLETADO) {
            pedido.setEstado(EstadoPedido.PAGADO);
            pedidoRepository.save(pedido);

            // Enviar confirmación por email @TODO
            //emailService.enviarConfirmacionPago(pedido, pagoGuardado);
        }

        return pagoGuardado;
    }

    private void procesarPagoTarjeta(Pago pago, PagoDTO dto) {
        // Enmascarar número de tarjeta
      //
    }

    private void procesarPagoPayPal(Pago pago, PagoDTO dto) {
        //
    }

    private void procesarPagoWebpay(Pago pago, PagoDTO dto) {
        //
    }

    private void procesarTransferencia(Pago pago, PagoDTO dto) {
        // Para transferencias, queda pendiente hasta confirmación manual
        pago.setEstadoPago(EstadoPago.PENDIENTE);
        //pago.setComentarios("Transferencia bancaria - Pendiente de confirmación");
        //pago.setNumeroTransferencia(dto.getNumeroTransferencia());
    }

    @Transactional
    public Pago confirmarTransferencia(Long pagoId, String numeroTransferencia) {
        Pago pago = pagoRepository.findById(pagoId)
                .orElseThrow(() -> new RuntimeException("Pago no encontrado"));

        if (pago.getEstadoPago() != EstadoPago.PENDIENTE) {
            throw new RuntimeException("Este pago no está pendiente de confirmación");
        }

        pago.setEstadoPago(EstadoPago.COMPLETADO);
       // pago.setNumeroTransferencia(numeroTransferencia);
        //pago.setFechaAprobacion(LocalDateTime.now());

        Pedido pedido = pago.getPedido();
        pedido.setEstado(EstadoPedido.PAGADO);
        pedidoRepository.save(pedido);

        return pagoRepository.save(pago);
    }




    private String generarCodigoTransaccion() {
        return "PAY-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }
}
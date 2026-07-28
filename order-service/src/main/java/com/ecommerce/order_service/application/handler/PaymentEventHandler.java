package com.ecommerce.order_service.application.handler;

import com.ecommerce.order_service.domain.model.Order;
import com.ecommerce.order_service.domain.port.out.OrderEventPublisherPort;
import com.ecommerce.order_service.domain.port.out.OrderRepositoryPort;
import com.ecommerce.order_service.domain.port.out.ProductServicePort;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class PaymentEventHandler {

    private final OrderRepositoryPort orderRepositoryPort;
    private final ProductServicePort productServicePort;
    private final OrderEventPublisherPort orderEventPublisherPort;

    @Transactional
    public void handlePaymentCompleted(Long orderId) {
        log.info("Ödeme tamamlandı eventi alındı. OrderId: {}", orderId);

        Order order = orderRepositoryPort.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Sipariş bulunamadı. OrderId: " + orderId
                ));

        // Stok düş — ödeme kesinleşti
        order.getItems().forEach(item ->
                productServicePort.reserveStock(item.getProductId(), item.getQuantity())
        );

        // Siparişi onayla — iş kuralı Order.confirm() içinde
        order.confirm();
        orderRepositoryPort.save(order);

        log.info("Sipariş onaylandı. OrderId: {}", orderId);
    }

    @Transactional
    public void handlePaymentFailed(Long orderId) {
        log.info("Ödeme başarısız eventi alındı. OrderId: {}", orderId);

        Order order = orderRepositoryPort.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Sipariş bulunamadı. OrderId: " + orderId
                ));

        // İptal et — iş kuralı Order.cancel() içinde
        order.cancel();
        orderRepositoryPort.save(order);

        log.info("Sipariş iptal edildi. OrderId: {}", orderId);
    }
}
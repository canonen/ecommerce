package com.ecommerce.order_service.application;

import com.ecommerce.order_service.domain.model.Order;
import com.ecommerce.order_service.domain.model.OrderItem;
import com.ecommerce.order_service.domain.port.in.CreateOrderCommand;
import com.ecommerce.order_service.domain.port.in.CreateOrderUseCase;
import com.ecommerce.order_service.domain.port.in.GetOrderUseCase;
import com.ecommerce.order_service.domain.port.out.OrderEventPublisherPort;
import com.ecommerce.order_service.domain.port.out.OrderRepositoryPort;
import com.ecommerce.order_service.domain.port.out.ProductServicePort;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderApplicationService implements CreateOrderUseCase, GetOrderUseCase {

    private final OrderRepositoryPort orderRepositoryPort;
    private final ProductServicePort productServicePort;
    private final OrderEventPublisherPort orderEventPublisherPort;

    @Override
    @Transactional
    public Order createOrder(CreateOrderCommand command) {
        log.info("Sipariş oluşturuluyor. UserId: {}", command.userId());

        // 1. Stok kontrolü — tüm ürünler için
        command.items().forEach(item -> {
            boolean available = productServicePort
                    .isStockAvailable(item.productId(), item.quantity());

            if (!available) {
                throw new IllegalStateException(
                        "Yetersiz stok. ProductId: " + item.productId()
                );
            }
        });

        Order order = Order.create(command.userId(), command.shippingAddress());

        command.items().forEach(item ->
                order.addItem(OrderItem.create(
                        item.productId(),
                        item.productName(),
                        item.quantity(),
                        item.unitPrice()
                ))
        );

        // 4. Kaydet
        Order savedOrder = orderRepositoryPort.save(order);
        log.info("Sipariş kaydedildi. OrderId: {}", savedOrder.getId());

        // 5. Event yayınla — Kafka'ya "order_created" gidecek
        // payment-service bunu dinleyip ödeme alacak
        orderEventPublisherPort.publishOrderCreated(savedOrder);
        log.info("OrderCreated eventi yayınlandı. OrderId: {}", savedOrder.getId());

        return savedOrder;
    }

    @Override
    public Order getOrderById(Long orderId) {
        return orderRepositoryPort.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Sipariş bulunamadı. OrderId: " + orderId
                ));
    }
}
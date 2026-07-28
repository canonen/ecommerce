package com.ecommerce.order_service.domain.port.out;

import com.ecommerce.order_service.domain.model.Order;

public interface OrderEventPublisherPort {
    void publishOrderCreated(Order order);
    void publishOrderCancelled(Order order);
}
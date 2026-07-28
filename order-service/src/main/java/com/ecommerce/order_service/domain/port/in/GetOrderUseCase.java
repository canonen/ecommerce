package com.ecommerce.order_service.domain.port.in;

import com.ecommerce.order_service.domain.model.Order;

public interface GetOrderUseCase {
    Order getOrderById(Long orderId);
}
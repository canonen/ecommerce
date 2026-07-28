package com.ecommerce.order_service.domain.port.in;

import com.ecommerce.order_service.domain.model.Order;

public interface CreateOrderUseCase {
    Order createOrder(CreateOrderCommand command);
}
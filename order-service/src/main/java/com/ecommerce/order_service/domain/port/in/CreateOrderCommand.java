package com.ecommerce.order_service.domain.port.in;

import java.math.BigDecimal;
import java.util.List;

public record CreateOrderCommand(
        Long userId,
        String shippingAddress,
        List<OrderItemCommand> items
) {
    public record OrderItemCommand(
            Long productId,
            String productName,
            Integer quantity,
            BigDecimal unitPrice
    ) {}
}
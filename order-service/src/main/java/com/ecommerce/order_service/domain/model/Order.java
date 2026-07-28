package com.ecommerce.order_service.domain.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Order {

    private Long id;
    private Long userId;
    private List<OrderItem> items;
    private OrderStatus status;
    private BigDecimal totalAmount;
    private String shippingAddress;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Private constructor
    private Order(Long userId, String shippingAddress) {
        this.userId = userId;
        this.shippingAddress = shippingAddress;
        this.items = new ArrayList<>();
        this.status = OrderStatus.PENDING; // her sipariş PENDING başlar
        this.totalAmount = BigDecimal.ZERO;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    private Order(Long id, Long userId, List<OrderItem> items,
                  OrderStatus status, BigDecimal totalAmount,
                  String shippingAddress, LocalDateTime createdAt,
                  LocalDateTime updatedAt) {
        this.id = id;
        this.userId = userId;
        this.items = new ArrayList<>(items);
        this.status = status;
        this.totalAmount = totalAmount;
        this.shippingAddress = shippingAddress;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static Order create(Long userId, String shippingAddress) {
        if (userId == null) {
            throw new IllegalArgumentException("UserId boş olamaz");
        }
        if (shippingAddress == null || shippingAddress.isBlank()) {
            throw new IllegalArgumentException("Teslimat adresi boş olamaz");
        }
        return new Order(userId, shippingAddress);
    }
    public static Order reconstruct(Long id, Long userId, List<OrderItem> items,
                                    OrderStatus status, BigDecimal totalAmount,
                                    String shippingAddress, LocalDateTime createdAt,
                                    LocalDateTime updatedAt) {
        return new Order(id, userId, items, status, totalAmount,
                shippingAddress, createdAt, updatedAt);
    }

    public void addItem(OrderItem item) {
        if (this.status != OrderStatus.PENDING) {
            throw new IllegalStateException(
                    "Sadece PENDING siparişe ürün eklenebilir"
            );
        }
        this.items.add(item);
        recalculateTotal();
        this.updatedAt = LocalDateTime.now();
    }

    public void confirm() {
        if (this.status != OrderStatus.PENDING) {
            throw new IllegalStateException(
                    "Sadece PENDING sipariş onaylanabilir. Mevcut durum: " + this.status
            );
        }
        if (this.items.isEmpty()) {
            throw new IllegalStateException("Boş sipariş onaylanamaz");
        }
        this.status = OrderStatus.CONFIRMED;
        this.updatedAt = LocalDateTime.now();
    }

    public void cancel() {
        if (this.status == OrderStatus.CONFIRMED) {
            throw new IllegalStateException("Onaylanmış sipariş iptal edilemez");
        }
        if (this.status == OrderStatus.CANCELLED) {
            throw new IllegalStateException("Sipariş zaten iptal edilmiş");
        }
        this.status = OrderStatus.CANCELLED;
        this.updatedAt = LocalDateTime.now();
    }

    private void recalculateTotal() {
        this.totalAmount = items.stream()
                .map(OrderItem::getTotalPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public Long getId() { return id; }
    public Long getUserId() { return userId; }
    public List<OrderItem> getItems() { return Collections.unmodifiableList(items); }
    public OrderStatus getStatus() { return status; }
    public BigDecimal getTotalAmount() { return totalAmount; }
    public String getShippingAddress() { return shippingAddress; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }

    // Infrastructure tarafından ID set etmek için
    public void setId(Long id) { this.id = id; }
}

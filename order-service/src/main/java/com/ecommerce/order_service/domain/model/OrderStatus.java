package com.ecommerce.order_service.domain.model;

public enum OrderStatus {
    PENDING,        // Sipariş oluşturuldu, ödeme bekleniyor
    CONFIRMED,      // Ödeme alındı, onaylandı
    CANCELLED,      // İptal edildi
    REFUNDED        // İade edildi
}

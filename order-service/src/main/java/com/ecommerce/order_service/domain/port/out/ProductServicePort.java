package com.ecommerce.order_service.domain.port.out;

public interface ProductServicePort {

    boolean isStockAvailable(Long productId, Integer quantity);

    // Sipariş onaylanınca stok düşülecek
    void reserveStock(Long productId, Integer quantity);

    // Sipariş iptal olunca stok iade edilecek
    void releaseStock(Long productId, Integer quantity);
}
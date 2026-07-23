package com.ecommerce.product_service.exception;

public class ProductNotFoundException extends RuntimeException{
    public ProductNotFoundException(Long id){
        super("Product bulunamadı. id : " + id);
    }
}

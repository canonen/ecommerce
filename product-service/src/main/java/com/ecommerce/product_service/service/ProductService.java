package com.ecommerce.product_service.service;

import com.ecommerce.product_service.dto.request.CreateProductRequest;
import com.ecommerce.product_service.dto.request.UpdateProductRequest;
import com.ecommerce.product_service.dto.request.UpdateStockRequest;
import com.ecommerce.product_service.dto.response.ProductResponse;
import com.ecommerce.product_service.entity.Category;
import com.ecommerce.product_service.entity.Product;
import com.ecommerce.product_service.exception.CategoryNotFoundException;
import com.ecommerce.product_service.exception.ProductNotFoundException;
import com.ecommerce.product_service.mapper.ProductMapper;
import com.ecommerce.product_service.repository.CategoryRepository;
import com.ecommerce.product_service.repository.ProductRepository;
import com.ecommerce.product_service.specification.ProductSpecification;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    private final ProductMapper productMapper;

    public ProductResponse createProduct(CreateProductRequest createProductRequest) {
        Category category = categoryRepository.findById(createProductRequest.getCategoryId())
                .orElseThrow(() -> new CategoryNotFoundException(createProductRequest.getCategoryId()));

        Product product = productMapper.toEntity(createProductRequest, category);
        productRepository.save(product);

        return productMapper.toResponse(product);
    }

    public Page<ProductResponse> getAllProducts(Long categoryId, BigDecimal minPrice, BigDecimal maxPrice, Pageable pageable) {
        Specification<Product> spec = ProductSpecification.filterBy(categoryId, minPrice, maxPrice);

        Page<Product> productPage = productRepository.findAll(spec, pageable);

        return productPage.map(productMapper::toResponse);
    }


    public ProductResponse getProductById(Long id) {
        return productRepository.findById(id).map(productMapper::toResponse).orElseThrow(()-> new ProductNotFoundException(id));
    }

    public String deleteByProductId(Long id) {
        //SOFT DELETE
        Product product = productRepository.findById(id).orElseThrow(() -> new ProductNotFoundException(id));
        product.setActive(false);
        productRepository.save(product);
        return "Success";
    }

    public Integer getStockByProductId(Long id) {
        return productRepository.findById(id).map(Product::getStock).orElseThrow(()-> new ProductNotFoundException(id));
    }

    @Transactional
    public ProductResponse updateStock(Long id, UpdateStockRequest request) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));

        int newStock = product.getStock() + request.getQuantity();

        if (newStock < 0) {
            throw new IllegalArgumentException(
                    "Yetersiz stok. Mevcut: " + product.getStock()
            );
        }

        product.setStock(newStock);
        Product saved = productRepository.save(product);

        return productMapper.toResponse(saved);
    }

    @Transactional
    public ProductResponse updateProduct(Long id, UpdateProductRequest request) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));

        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setPrice(request.getPrice());
        product.setImageUrl(request.getImageUrl());

        if (request.getActive() != null) {
            product.setActive(request.getActive());
        }

        Product saved = productRepository.save(product);
        return productMapper.toResponse(saved);
    }
}

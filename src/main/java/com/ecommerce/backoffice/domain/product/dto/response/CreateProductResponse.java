package com.ecommerce.backoffice.domain.product.dto.response;

import com.ecommerce.backoffice.domain.product.entity.Product;
import com.ecommerce.backoffice.domain.product.enums.ProductCategory;
import com.ecommerce.backoffice.domain.product.enums.ProductStatus;

import java.time.LocalDateTime;

public record CreateProductResponse(
        Long id,
        String name,
        ProductCategory category,
        Long price,
        Integer stock,
        ProductStatus status,
        LocalDateTime createdAt
) {
    public static CreateProductResponse from(Product product){
        return new CreateProductResponse(
                product.getId(),
                product.getName(),
                product.getCategory(),
                product.getPrice(),
                product.getStock(),
                product.getStatus(),
                product.getCreatedAt()
        );
    }
}

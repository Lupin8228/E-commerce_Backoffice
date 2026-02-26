package com.ecommerce.backoffice.domain.product.dto.response;

import com.ecommerce.backoffice.domain.product.entity.Product;
import com.ecommerce.backoffice.domain.product.enums.ProductCategory;
import com.ecommerce.backoffice.domain.product.enums.ProductStatus;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record CreateProductResponse(
        Long id,
        String name,
        ProductCategory category,
        Long price,
        int stock,
        ProductStatus status,
        LocalDateTime createdAt
) {
    public static CreateProductResponse from(Product product){
        return CreateProductResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .category(product.getCategory())
                .price(product.getPrice())
                .stock(product.getStock())
                .status(product.getStatus())
                .createdAt(product.getCreatedAt())
                .build();
    }
}

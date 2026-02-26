package com.ecommerce.backoffice.domain.product.dto.response;

import com.ecommerce.backoffice.domain.product.entity.Product;
import com.ecommerce.backoffice.domain.product.enums.ProductCategory;
import com.ecommerce.backoffice.domain.product.enums.ProductStatus;

import java.time.LocalDateTime;

public record GetDetailProductResponse(
        Long id,
        String name,
        ProductCategory category,
        Long price,
        int stock,
        ProductStatus status,
        LocalDateTime createdAt
        //등록 관리자 이메일
) {
    public static GetDetailProductResponse from(Product product) {
        return new GetDetailProductResponse(
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

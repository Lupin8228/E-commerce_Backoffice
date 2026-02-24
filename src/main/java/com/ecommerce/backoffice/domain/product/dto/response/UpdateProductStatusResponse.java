package com.ecommerce.backoffice.domain.product.dto.response;

import com.ecommerce.backoffice.domain.product.entity.Product;
import com.ecommerce.backoffice.domain.product.enums.ProductStatus;

public record UpdateProductStatusResponse(
        Long id,
        String name,
        Long price,
        int stock,
        ProductStatus status
) {
    public static UpdateProductStatusResponse from(Product product){
        return new UpdateProductStatusResponse(
                product.getId(),
                product.getName(),
                product.getPrice(),
                product.getStock(),
                product.getStatus()
        );
    }
}

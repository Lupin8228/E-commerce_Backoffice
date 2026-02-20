package com.ecommerce.backoffice.domain.product.dto.response;

import com.ecommerce.backoffice.domain.product.entity.Product;
import com.ecommerce.backoffice.domain.product.enums.ProductCategory;
import com.ecommerce.backoffice.domain.product.enums.ProductStatus;

public record GetAllProductResponse(
        Long id,
        String name,
        ProductCategory category,
        Long price,
        Integer stock,
        ProductStatus status
) {
    public static GetAllProductResponse from(Product product){
        return new GetAllProductResponse(
                product.getId(),
                product.getName(),
                product.getCategory(),
                product.getPrice(),
                product.getStock(),
                product.getStatus()
        );
    }
}

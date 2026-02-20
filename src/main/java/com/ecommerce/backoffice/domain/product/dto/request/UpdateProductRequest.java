package com.ecommerce.backoffice.domain.product.dto.request;

import com.ecommerce.backoffice.domain.admin.entity.Admin;
import com.ecommerce.backoffice.domain.product.entity.Product;
import com.ecommerce.backoffice.domain.product.enums.ProductCategory;

public record UpdateProductRequest(
        String name,
        ProductCategory category,
        Long price
) {

}

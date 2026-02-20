package com.ecommerce.backoffice.domain.product.dto.request;


import com.ecommerce.backoffice.domain.admin.entity.Admin;
import com.ecommerce.backoffice.domain.product.entity.Product;
import com.ecommerce.backoffice.domain.product.enums.ProductCategory;
import com.ecommerce.backoffice.domain.product.enums.ProductStatus;

public record CreateProductRequest (
        String name,
        ProductCategory category,
        Long price,
        Integer stock,
        ProductStatus status
) {
    public Product toProduct(Admin admin) {
        return new Product(
                this.name,
                this.category,
                this.price,
                this.stock,
                this.status,
                admin // 외부에서 전달받은 관리자 정보 주입
        );
    }
}

package com.ecommerce.backoffice.domain.product.dto.request;

import com.ecommerce.backoffice.domain.product.enums.ProductCategory;
import com.ecommerce.backoffice.domain.product.enums.ProductStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateProductRequest (
        @NotBlank(message = "상품명은 필수 입력 항목입니다.")
        String name,

        ProductCategory category,

        @NotBlank(message = "가격은 필수 입력 항목입니다.")
        @Size(min = 10, message = "가격은 10원 이상이어야 합니다.")
        Long price,

        @NotBlank(message = "재고는 필수 입력 항목입니다.")
        @Size(min = 0, message = "재고는 0 이하로 입력할 수 없습니다.")
        int stock,

        ProductStatus status
) {
}
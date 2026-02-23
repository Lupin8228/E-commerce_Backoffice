package com.ecommerce.backoffice.domain.product.dto.request;

import com.ecommerce.backoffice.domain.product.enums.ProductCategory;
import com.ecommerce.backoffice.domain.product.enums.ProductStatus;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CreateProductRequest (
        @NotBlank(message = "상품명은 필수 입력 항목입니다.")
        String name,

        @NotNull(message = "카테고리는 필수 입력 항목입니다.")
        ProductCategory category,

        @NotNull(message = "가격은 필수 입력 항목입니다.")
        @Min(value = 10, message = "가격은 10원 이상이어야 합니다.")
        Long price,

        @NotNull(message = "재고는 필수 입력 항목입니다.")
        @Min(value = 0, message = "재고는 0 이하로 입력할 수 없습니다.")
        int stock,

        @NotNull(message = "상태는 필수 입력 항목입니다.")
        ProductStatus status
) {
}
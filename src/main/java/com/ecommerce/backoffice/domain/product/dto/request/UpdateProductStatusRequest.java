package com.ecommerce.backoffice.domain.product.dto.request;

import com.ecommerce.backoffice.domain.product.enums.ProductStatus;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record UpdateProductStatusRequest(
        @NotNull(message = "카테고리는 필수 입력 항목입니다.")
        ProductStatus status,

        @NotNull(message = "가격은 필수 입력 항목입니다.")
        @Min(value = 0, message = "재고는 0 이상이어야 합니다.")
        int stock
) {
}

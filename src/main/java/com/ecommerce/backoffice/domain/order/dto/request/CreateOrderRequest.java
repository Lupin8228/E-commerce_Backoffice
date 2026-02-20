package com.ecommerce.backoffice.domain.order.dto.request;

import lombok.Builder;

@Builder
public record CreateOrderRequest(
        // 사용자 정보
        Long customerId,
        // 상품 정보
        Long productId,
        Integer quantity
) {
}

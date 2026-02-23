package com.ecommerce.backoffice.domain.order.dto.response;

import lombok.Builder;

@Builder
public record CancelOrderResponse (
        String orderNumber,
        String message
) {
    public static CancelOrderResponse from(String orderNumber) {
        return CancelOrderResponse.builder()
                .orderNumber(orderNumber)
                .message(orderNumber+" 주문이 취소되었습니다.")
                .build();
    }
}

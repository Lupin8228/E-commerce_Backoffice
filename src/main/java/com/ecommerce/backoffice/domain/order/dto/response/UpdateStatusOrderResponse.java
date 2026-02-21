package com.ecommerce.backoffice.domain.order.dto.response;

import com.ecommerce.backoffice.domain.order.enums.OrderStatus;
import lombok.Builder;

@Builder
public record UpdateStatusOrderResponse (
        String orderNumber,
        OrderStatus status,
        String message
) {
    public static UpdateStatusOrderResponse of(String orderNumber, OrderStatus status) {
        return UpdateStatusOrderResponse.builder()
                .orderNumber(orderNumber)
                .status(status)
                .message(orderNumber+" 주문 상태가 "+ status.getDescription()+"으로 변경되었습니다.")
                .build();
    }
}

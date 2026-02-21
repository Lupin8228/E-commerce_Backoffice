package com.ecommerce.backoffice.domain.dashboard.dto.recentOrder;

import com.ecommerce.backoffice.domain.order.enums.OrderStatus;

import lombok.Builder;

@Builder
public record RecentOrderResponse(
        String orderNumber,
        String customerName,
        String productName,
        Long totalPrice,
        OrderStatus status
) {

}

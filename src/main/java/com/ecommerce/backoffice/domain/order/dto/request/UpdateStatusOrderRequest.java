package com.ecommerce.backoffice.domain.order.dto.request;

import com.ecommerce.backoffice.domain.order.enums.OrderStatus;
import lombok.Builder;

@Builder
public record UpdateStatusOrderRequest (
        OrderStatus status
) {

}

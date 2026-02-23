package com.ecommerce.backoffice.domain.order.dto.request;

import com.ecommerce.backoffice.domain.order.enums.OrderStatus;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record UpdateStatusOrderRequest (
        @NotBlank(message = "주문 상태는 필수 입력값입니다.")
        OrderStatus status
) {

}

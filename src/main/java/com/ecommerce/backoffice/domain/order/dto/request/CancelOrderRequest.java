package com.ecommerce.backoffice.domain.order.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record CancelOrderRequest (
        @NotBlank(message = "취소 사유는 필수 입력값입니다.")
        String cancelReason
) {

}

package com.ecommerce.backoffice.domain.order.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;

@Builder
public record CancelOrderRequest (
        @NotBlank(message = "취소 사유는 필수 입력값입니다.")
        @Size(max = 500, message = "취소 사유는 500자 이하여야 합니다.")
        String cancelReason
) {

}

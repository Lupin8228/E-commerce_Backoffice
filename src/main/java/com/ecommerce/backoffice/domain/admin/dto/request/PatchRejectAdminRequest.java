package com.ecommerce.backoffice.domain.admin.dto.request;

import jakarta.validation.constraints.NotBlank;

public record PatchRejectAdminRequest(

        @NotBlank(message = "거부 사유는 필수입니다.")
        String reason
) {
}


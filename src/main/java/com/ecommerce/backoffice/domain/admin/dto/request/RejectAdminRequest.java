package com.ecommerce.backoffice.domain.admin.dto.request;

import jakarta.validation.constraints.NotBlank;

public record RejectAdminRequest(

        @NotBlank(message = "거부 사유는 필수입니다.")
        String reason
) {}


package com.ecommerce.backoffice.domain.admin.dto.request;

import com.ecommerce.backoffice.domain.admin.enums.AdminStatus;
import jakarta.validation.constraints.NotNull;

public record PatchAdminStatusChangeRequest(
        @NotNull(message = "상태는 필수입니다.")
        AdminStatus status
) {
}

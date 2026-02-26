package com.ecommerce.backoffice.domain.admin.dto.request;


import com.ecommerce.backoffice.domain.admin.enums.AdminRole;
import jakarta.validation.constraints.NotNull;

public record UpdateAdminRoleRequest(

        @NotNull(message = "변경할 Role의 입력값은 필수입니다.")
        AdminRole role
) {
}

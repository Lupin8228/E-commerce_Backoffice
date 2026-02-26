package com.ecommerce.backoffice.domain.admin.dto.response;

import com.ecommerce.backoffice.domain.admin.enums.AdminRole;

public record UpdateAdminRoleResponse(
        Long id,
        AdminRole role
) {
}

package com.ecommerce.backoffice.domain.admin.dto.session;

import com.ecommerce.backoffice.domain.admin.enums.AdminRole;

public record SessionAdmin(
        Long id,
        String email,
        AdminRole role
) {}

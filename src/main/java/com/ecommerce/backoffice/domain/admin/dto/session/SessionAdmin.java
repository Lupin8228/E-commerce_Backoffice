package com.ecommerce.backoffice.domain.admin.dto.session;

import com.ecommerce.backoffice.domain.admin.entity.Admin;
import com.ecommerce.backoffice.domain.admin.enums.AdminRole;

public record SessionAdmin(
        Long id,
        String name,
        String email,
        AdminRole role
) {
    public static SessionAdmin from(Admin admin) {
        return new SessionAdmin(
                admin.getId(),
                admin.getName(),
                admin.getEmail(),
                admin.getRole()
        );
    }
}

package com.ecommerce.backoffice.domain.admin.dto.response;

import com.ecommerce.backoffice.domain.admin.entity.Admin;
import com.ecommerce.backoffice.domain.admin.enums.AdminRole;
import lombok.Builder;

@Builder
public record AdminLoginResponse(
        Long id,
        String name,
        String email,
        AdminRole role
) {
    public static AdminLoginResponse from(Admin admin) {
        return AdminLoginResponse.builder()
                .id(admin.getId())
                .name(admin.getName())
                .email(admin.getEmail())
                .role(admin.getRole())
                .build();
    }
}
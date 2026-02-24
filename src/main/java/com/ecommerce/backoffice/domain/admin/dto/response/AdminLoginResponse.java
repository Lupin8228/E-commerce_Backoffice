package com.ecommerce.backoffice.domain.admin.dto.response;

import com.ecommerce.backoffice.domain.admin.entity.Admin;
import com.ecommerce.backoffice.domain.admin.enums.AdminRole;
import lombok.Builder;

@Builder
public record AdminLoginResponse(
        Long id,
        String name,
        String email,
        AdminRole role,
        String token
) {
    public static AdminLoginResponse from(Admin admin, String token) {
        return AdminLoginResponse.builder()
                .id(admin.getId())
                .name(admin.getName())
                .email(admin.getEmail())
                .role(admin.getRole())
                .token(token)
                .build();
    }
}
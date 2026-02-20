package com.ecommerce.backoffice.domain.admin.dto.response;

import com.ecommerce.backoffice.domain.admin.entity.Admin;
import com.ecommerce.backoffice.domain.admin.enums.AdminRole;
import com.ecommerce.backoffice.domain.admin.enums.AdminStatus;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record AdminSignUpResponse(
        Long id,
        String name,
        String email,
        String phone,
        AdminRole role,
        AdminStatus status,
        LocalDateTime createdAt
) {
    public static AdminSignUpResponse from(Admin admin) {
        return AdminSignUpResponse.builder()
                .id(admin.getId())
                .name(admin.getName())
                .email(admin.getEmail())
                .phone(admin.getPhone())
                .role(admin.getRole())
                .status(admin.getStatus())
                .createdAt(admin.getCreatedAt())
                .build();
    }
}
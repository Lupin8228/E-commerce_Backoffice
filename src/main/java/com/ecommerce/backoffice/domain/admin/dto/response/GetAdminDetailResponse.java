package com.ecommerce.backoffice.domain.admin.dto.response;

import com.ecommerce.backoffice.domain.admin.entity.Admin;
import com.ecommerce.backoffice.domain.admin.enums.AdminRole;
import com.ecommerce.backoffice.domain.admin.enums.AdminStatus;

import java.time.LocalDateTime;

public record GetAdminDetailResponse(
        Long id,
        String name,
        String email,
        String phone,
        AdminRole role,
        AdminStatus status,
        LocalDateTime createdAt,   // 가입일
        LocalDateTime approvedAt   // 승인일
) {
    public static GetAdminDetailResponse from(Admin admin) {
        return new GetAdminDetailResponse(
                admin.getId(),
                admin.getName(),
                admin.getEmail(),
                admin.getPhone(),
                admin.getRole(),
                admin.getStatus(),
                admin.getCreatedAt(),
                admin.getApprovedAt()
        );
    }
}

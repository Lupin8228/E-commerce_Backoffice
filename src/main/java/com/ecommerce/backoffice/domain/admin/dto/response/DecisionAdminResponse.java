package com.ecommerce.backoffice.domain.admin.dto.response;

import com.ecommerce.backoffice.domain.admin.entity.Admin;
import com.ecommerce.backoffice.domain.admin.enums.AdminStatus;

import java.time.LocalDateTime;

public record DecisionAdminResponse(
        Long id,
        AdminStatus status,
        LocalDateTime approvedAt,
        LocalDateTime rejectedAt,
        String rejectedReason
) {
    public static DecisionAdminResponse from(Admin admin) {
        return new DecisionAdminResponse(
                admin.getId(),
                admin.getStatus(),
                admin.getApprovedAt(),
                admin.getRejectedAt(),
                admin.getRejectedReason()
        );
    }
}

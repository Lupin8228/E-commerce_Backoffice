package com.ecommerce.backoffice.domain.admin.dto.response;

import com.ecommerce.backoffice.domain.admin.entity.Admin;
import com.ecommerce.backoffice.domain.admin.enums.AdminStatus;

import java.time.LocalDateTime;

public record PatchDecisionAdminResponse(
        Long id,
        AdminStatus status,
        LocalDateTime approvedAt,
        LocalDateTime rejectedAt,
        String rejectedReason
) {
    public static PatchDecisionAdminResponse from(Admin admin) {
        return new PatchDecisionAdminResponse(
                admin.getId(),
                admin.getStatus(),
                admin.getApprovedAt(),
                admin.getRejectedAt(),
                admin.getRejectedReason()
        );
    }
}

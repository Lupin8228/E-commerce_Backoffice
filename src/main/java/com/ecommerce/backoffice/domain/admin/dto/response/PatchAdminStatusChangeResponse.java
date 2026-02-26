package com.ecommerce.backoffice.domain.admin.dto.response;

import com.ecommerce.backoffice.domain.admin.entity.Admin;
import com.ecommerce.backoffice.domain.admin.enums.AdminStatus;

import java.time.LocalDateTime;

public record PatchAdminStatusChangeResponse(
        Long id,
        AdminStatus status,
        LocalDateTime approvedAt,
        LocalDateTime rejectedAt
) {
    public static PatchAdminStatusChangeResponse from(Admin admin) {
        return new PatchAdminStatusChangeResponse(
                admin.getId(),
                admin.getStatus(),
                admin.getApprovedAt(),
                admin.getRejectedAt()
        );
    }
}

package com.ecommerce.backoffice.domain.admin.dto.response;

import com.ecommerce.backoffice.domain.admin.entity.AdminStatus;

import java.time.LocalDateTime;

public record DecisionAdminResponse(
        Long id,
        AdminStatus status,
        LocalDateTime approvedAt,
        LocalDateTime rejectedAt,
        String rejectedReason
) {}

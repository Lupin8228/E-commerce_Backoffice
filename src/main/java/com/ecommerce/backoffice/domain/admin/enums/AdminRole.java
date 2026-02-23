package com.ecommerce.backoffice.domain.admin.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum AdminRole {
    SUPER_ADMIN,
    OPERATION_ADMIN,
    CS_ADMIN
}


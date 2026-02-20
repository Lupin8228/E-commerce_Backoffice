package com.ecommerce.backoffice.domain.admin.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * PENDING    : 승인 대기
 * APPROVED   : 승인
 * REJECTED   : 승인 거절
 * SUSPENDED  : 권한 정지
 * INACTIVATE : 비활성
 */
@Getter
@RequiredArgsConstructor
public enum AdminStatus {

    PENDING("승인 대기"),
    APPROVED("승인"),
    REJECTED("승인 거절"),
    SUSPENDED("권한 정지"),
    INACTIVATE("비활성");

    private final String description;
}
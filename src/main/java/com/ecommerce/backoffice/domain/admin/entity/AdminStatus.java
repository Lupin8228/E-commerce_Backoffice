package com.ecommerce.backoffice.domain.admin.entity;


import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum AdminStatus {
    PENDING,
    APPROVED,
    REJECTED,
    SUSPENDED,
    INACTIVE
}
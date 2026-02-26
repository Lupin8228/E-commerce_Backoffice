package com.ecommerce.backoffice.global.common;

public record BaseErrorResponse(
        int status,
        String code,
        String errorMessage
) {}

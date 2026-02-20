package com.ecommerce.backoffice.domain.admin.dto.response;

public record GetProfileResponse(
        String name,
        String email,
        String phone
) {}

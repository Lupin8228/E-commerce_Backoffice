package com.ecommerce.backoffice.domain.admin.dto.response;

public record UpdateProfileResponse(
        String name,
        String email,
        String phone
) {}

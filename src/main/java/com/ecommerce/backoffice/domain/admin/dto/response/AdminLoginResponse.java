package com.ecommerce.backoffice.domain.admin.dto.response;

import com.ecommerce.backoffice.domain.admin.entity.Admin;
import com.ecommerce.backoffice.domain.admin.enums.AdminRole;
import lombok.Builder;
import lombok.Getter;

@Getter
public class AdminLoginResponse {

    private final Long id;
    private final String name;
    private final String email;
    private final AdminRole role;

    @Builder
    private AdminLoginResponse(Long id, String name, String email, AdminRole role) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.role = role;
    }

    public static AdminLoginResponse from(Admin admin) {
        return AdminLoginResponse.builder()
                .id(admin.getId())
                .name(admin.getName())
                .email(admin.getEmail())
                .role(admin.getRole())
                .build();
    }
}
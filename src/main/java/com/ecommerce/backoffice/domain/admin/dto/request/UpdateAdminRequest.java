package com.ecommerce.backoffice.domain.admin.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;


public record UpdateAdminRequest(

        @Size(min = 2, max = 50)
        String name,

        @Email
        @Size(max = 100)
        String email,

        @Pattern(regexp = "^010-\\d{4}-\\d{4}$")
        String phone
) {}
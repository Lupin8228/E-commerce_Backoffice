package com.ecommerce.backoffice.domain.admin.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record UpdateProfileRequest(

        @Size(max = 50, message = "이름은 최대 50자까지 가능합니다.")
        String name,

        @Email(message = "이메일 형식이 올바르지 않습니다.")
        String email,

        @Pattern(regexp = "^010-\\d{4}-\\d{4}$", message = "전화번호는 010-XXXX-XXXX 형식이어야 합니다.")
        String phone
) {
}

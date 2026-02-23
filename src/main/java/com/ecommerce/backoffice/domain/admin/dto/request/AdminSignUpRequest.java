package com.ecommerce.backoffice.domain.admin.dto.request;

import com.ecommerce.backoffice.domain.admin.enums.AdminRole;
import jakarta.validation.constraints.*;

public record AdminSignUpRequest(
        @NotBlank(message = "이름은 필수 입력 항목입니다.")
        String name,

        @NotBlank(message = "이메일은 필수 입력 항목입니다.")
        @Email(message = "올바른 이메일 형식이 아닙니다.")
        String email,

        @NotBlank(message = "비밀번호는 필수 입력 항목입니다.")
        @Size(min = 8, message = "비밀번호는 최소 8자 이상이어야 합니다.")
        String password,

        @NotBlank(message = "전화번호는 필수 입력 항목입니다.")
        @Pattern(regexp = "^010\\d{8}$", message = "전화번호 형식(010XXXXXXXX)을 확인해주세요.")
        String phone,

        @NotNull(message = "역할 선택은 필수입니다.") // Enum이므로 @NotBlank 대신 @NotNull 사용
        AdminRole role

) {
}

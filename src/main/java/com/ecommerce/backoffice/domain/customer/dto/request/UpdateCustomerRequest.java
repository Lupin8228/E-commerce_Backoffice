package com.ecommerce.backoffice.domain.customer.dto.request;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record UpdateCustomerRequest(
        @Size(max = 50, message = "이름은 50자 이하여야 합니다.")
        String name,

        @Size(max = 100, message = "email은 100자 이하여야 합니다.")
        @Email(message = "올바른 이메일 형식이어야 합니다.")
        String email,

        @Size(max = 13, message = "전화번호는 13자 이하여야 합니다.")
        @Pattern(regexp = "^\\+?\\d{1,3}?[- .]?\\d{1,4}?[- .]?\\d{4,10}$",
                message = "올바른 전화번호 형식이어야 합니다.")
        String phone
) {
        public boolean isAllBlank() {
                return isBlank(name)
                        && isBlank(email)
                        && isBlank(phone);
        }

        private boolean isBlank(String value) {
                return value == null || value.isBlank();
        }
}

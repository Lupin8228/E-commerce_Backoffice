package com.ecommerce.backoffice.domain.customer.dto.request;


import jakarta.validation.constraints.Size;

public record UpdateCustomerRequest(
        @Size(max = 50, message = "이름은 50자 이하여야 합니다.")
        String name,
        @Size(max = 100, message = "email은 100자 이하여야 합니다.")
        String email,
        @Size(max = 13, message = "전화번호는 13자 이하여야 합니다.")
        String phone
) {
}

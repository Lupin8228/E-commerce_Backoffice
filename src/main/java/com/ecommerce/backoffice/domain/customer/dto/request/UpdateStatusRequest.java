package com.ecommerce.backoffice.domain.customer.dto.request;

import com.ecommerce.backoffice.domain.customer.enums.CustomerStatus;
import com.ecommerce.backoffice.global.error.CommonError;
import com.ecommerce.backoffice.global.error.CommonException;
import jakarta.validation.constraints.NotBlank;

public record UpdateStatusRequest(
        @NotBlank(message = "status는 필수입니다.")
        String status
) {
    public CustomerStatus toCustomerStatus() {
        try {
            return CustomerStatus.valueOf(status.toUpperCase());
        } catch (IllegalArgumentException | NullPointerException e) {
            throw new CommonException(CommonError.INVALID_CUSTOMER_UPDATE);
        }
    }
}

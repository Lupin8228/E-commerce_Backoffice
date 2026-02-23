package com.ecommerce.backoffice.domain.customer.dto.request;

import com.ecommerce.backoffice.domain.customer.enums.CustomerStatus;

public record UpdateStatusRequest(
        CustomerStatus status
) {
}

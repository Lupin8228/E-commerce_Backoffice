package com.ecommerce.backoffice.domain.customer.dto.response;

import com.ecommerce.backoffice.domain.customer.entity.Customer;
import com.ecommerce.backoffice.domain.customer.enums.CustomerStatus;

import java.time.LocalDateTime;

public record GetCustomerResponse(
        Long id,
        String name,
        String email,
        String phone,
        CustomerStatus status,
        LocalDateTime createdAt,
        Long totalOrderCount,
        Long totalOrderAmount
) {

}

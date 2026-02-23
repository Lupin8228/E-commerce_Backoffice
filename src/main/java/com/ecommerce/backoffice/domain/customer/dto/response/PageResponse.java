package com.ecommerce.backoffice.domain.customer.dto.response;

import com.ecommerce.backoffice.domain.customer.entity.Customer;
import org.springframework.data.domain.Page;

import java.util.List;

public record PageResponse<T>(
        List<T> content,
        int page,
        int size,
        long totalElements,
        int totalPages
) {

    public static PageResponse<GetCustomerResponse> from(Page<GetCustomerResponse> page) {

        return new PageResponse<>(
                page.getContent(),
                page.getNumber()+1,
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages());
    }
}
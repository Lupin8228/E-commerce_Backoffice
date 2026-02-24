package com.ecommerce.backoffice.domain.customer.repository;

import com.ecommerce.backoffice.domain.customer.dto.response.GetCustomerResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CustomerRepositoryCustom {
    Page<GetCustomerResponse> searchWithOrderStats(String search, Pageable pageable);
    GetCustomerResponse findByIdWithOrderStats(Long customerId);
}
package com.ecommerce.backoffice.domain.customer.repository;

import com.ecommerce.backoffice.domain.customer.dto.response.GetCustomerResponse;
import com.ecommerce.backoffice.domain.customer.entity.Customer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CustomerRepositoryCustom {
//    Page<Customer> search(String search, Pageable pageable);
    Page<GetCustomerResponse> searchWithOrderStats(String search, Pageable pageable);
    GetCustomerResponse findByIdWithOrderStats(Long customerId);
}
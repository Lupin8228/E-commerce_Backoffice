package com.ecommerce.backoffice.domain.customer.repository;

import com.ecommerce.backoffice.domain.customer.entity.Customer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CustomerRepositoryCustom {
    Page<Customer> search(String search, Pageable pageable);
}
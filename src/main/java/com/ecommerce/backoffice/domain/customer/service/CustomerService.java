package com.ecommerce.backoffice.domain.customer.service;

import com.ecommerce.backoffice.domain.customer.dto.response.GetCustomerResponse;
import com.ecommerce.backoffice.domain.customer.dto.response.PageResponse;
import com.ecommerce.backoffice.domain.customer.entity.Customer;
import com.ecommerce.backoffice.domain.customer.repository.CustomerRepository;
import com.ecommerce.backoffice.global.error.CommonError;
import com.ecommerce.backoffice.global.error.CommonException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CustomerService {
    private final CustomerRepository customerRepository;

    // 전체 조회
    public PageResponse<GetCustomerResponse> getCustomers(Pageable pageable) {
        Page<GetCustomerResponse> page = customerRepository.findAll(pageable).map(GetCustomerResponse::from);
        return PageResponse.from(page);


    }
    // 단건 조회
    public GetCustomerResponse getCustomer(Long id) {

        Customer customer = customerRepository.findByIdAndDeletedFalse(id).orElseThrow(
                ()->new CommonException(CommonError.CUSTOMER_NOT_FOUND));
        return GetCustomerResponse.from(customer);
    }
}

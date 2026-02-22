package com.ecommerce.backoffice.domain.customer.controller;


import com.ecommerce.backoffice.domain.customer.dto.response.GetCustomerResponse;
import com.ecommerce.backoffice.domain.customer.dto.response.PageResponse;
import com.ecommerce.backoffice.domain.customer.enums.CustomerSortField;
import com.ecommerce.backoffice.domain.customer.service.CustomerService;
import com.ecommerce.backoffice.global.common.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class CustomerController {
    private final CustomerService customerService;

    // 전체 조회(정렬만 구현, 이름, 이메일로 검색하는 기능은 아직 구현 안함)
    @GetMapping("/api/customers")
    public ResponseEntity<ApiResponse<PageResponse<GetCustomerResponse>>> getCustomers(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "CREATED_AT") String sortBy,
            @RequestParam(defaultValue = "desc") String sortOrder) {
        // 정렬 조건
        String sortField = CustomerSortField.from(sortBy);
        // 정렬 기준
        Sort.Direction direction =
                sortOrder.equalsIgnoreCase("asc")
                        ? Sort.Direction.ASC
                        : Sort.Direction.DESC;

        Pageable pageable =
                PageRequest.of(page - 1, size, Sort.by(direction, sortField));

        PageResponse<GetCustomerResponse> response =
                customerService.getCustomers(pageable);

        return ResponseEntity.ok(ApiResponse.success(response));
    }
    // 단건 조회
    @GetMapping("/api/customers/{id}")
    public ResponseEntity<ApiResponse<GetCustomerResponse>> getCustomer(@PathVariable Long id)
    {
        return ResponseEntity.ok(ApiResponse.success(customerService.getCustomer(id)));
    }

}

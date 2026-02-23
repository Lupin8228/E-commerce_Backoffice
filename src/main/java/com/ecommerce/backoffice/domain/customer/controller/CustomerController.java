package com.ecommerce.backoffice.domain.customer.controller;


import com.ecommerce.backoffice.domain.customer.dto.request.UpdateCustomerRequest;
import com.ecommerce.backoffice.domain.customer.dto.request.UpdateStatusRequest;
import com.ecommerce.backoffice.domain.customer.dto.response.GetCustomerResponse;
import com.ecommerce.backoffice.domain.customer.dto.response.PageResponse;
import com.ecommerce.backoffice.domain.customer.enums.CustomerSortField;
import com.ecommerce.backoffice.domain.customer.service.CustomerService;
import com.ecommerce.backoffice.global.common.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
public class CustomerController {
    private final CustomerService customerService;

    @GetMapping("/api/customers")
    public ResponseEntity<ApiResponse<PageResponse<GetCustomerResponse>>> getCustomers(
            @RequestParam(required = false) String search,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "CREATED_AT") CustomerSortField sortBy, // ENUM 타입(대문자)만 가능
            @RequestParam(defaultValue = "desc") String sortOrder) {
        // TODO: 정렬 기준 => 얘도 Enum 처리 할지 고민중
        Sort.Direction direction =
                sortOrder.equalsIgnoreCase("asc")
                        ? Sort.Direction.ASC
                        : Sort.Direction.DESC;

        Pageable pageable =
                PageRequest.of(page - 1, size, Sort.by(direction, sortBy.getField()));

        PageResponse<GetCustomerResponse> response =
                customerService.getCustomers(search,pageable);

        return ResponseEntity.ok(ApiResponse.success(response));
    }
    // 단건 조회
    @GetMapping("/api/customers/{id}")
    public ResponseEntity<ApiResponse<GetCustomerResponse>> getCustomer(@PathVariable Long id)
    {
        return ResponseEntity.ok(ApiResponse.success(customerService.getCustomer(id)));
    }

    // 정보 업데이트
    @PatchMapping("/api/customers/{id}")
    public ResponseEntity<ApiResponse<GetCustomerResponse>> updateCustomer(
            @PathVariable Long id,
            @Valid @RequestBody UpdateCustomerRequest request
    ) {

        return ResponseEntity.ok(ApiResponse.success(customerService.updateCustomer(id,request)));
    }

    // 상태 업데이트
    @PatchMapping("/api/customers/{id}/status")
    public ResponseEntity<ApiResponse<GetCustomerResponse>> updateCustomerStatus(
            @PathVariable Long id,
            @Valid @RequestBody UpdateStatusRequest request
    ) {
        return ResponseEntity.ok(ApiResponse.success(customerService.updateStatus(id,request)));
    }

    // 삭제
    @DeleteMapping("/api/customers/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteCustomer(@PathVariable Long id){
        customerService.deleteCustomer(id);
        return ResponseEntity.ok(ApiResponse.success(null));
    }

}

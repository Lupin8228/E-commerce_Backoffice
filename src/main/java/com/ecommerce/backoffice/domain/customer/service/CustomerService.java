package com.ecommerce.backoffice.domain.customer.service;

import com.ecommerce.backoffice.domain.customer.dto.request.UpdateCustomerRequest;
import com.ecommerce.backoffice.domain.customer.dto.request.UpdateStatusRequest;
import com.ecommerce.backoffice.domain.customer.dto.response.GetCustomerResponse;
import com.ecommerce.backoffice.domain.customer.dto.response.PageResponse;
import com.ecommerce.backoffice.domain.customer.entity.Customer;
import com.ecommerce.backoffice.domain.customer.enums.CustomerStatus;
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
    public PageResponse<GetCustomerResponse> getCustomers(String search, Pageable pageable) {
        Page<GetCustomerResponse> page = customerRepository.searchWithOrderStats(search,pageable);

        return PageResponse.from(page);
    }

    // 단건 조회
    public GetCustomerResponse getCustomer(Long id) {
        boolean existence = customerRepository.existsById(id);
        if(!existence){
            throw new CommonException(CommonError.CUSTOMER_NOT_FOUND);
        }
        return customerRepository.findByIdWithOrderStats(id);
    }

    // 정보 업데이트(이름,이메일,전화번호)
    @Transactional
    public GetCustomerResponse updateCustomer(Long id, UpdateCustomerRequest request) {

        Customer customer = customerRepository.findByIdAndDeletedFalse(id).orElseThrow(
                ()->new CommonException(CommonError.CUSTOMER_NOT_FOUND));

        // 업데이트 할 데이터가 존재하지 않는 경우 에러
        if (request.isAllBlank()) {
            throw new CommonException(CommonError.INVALID_CUSTOMER_UPDATE);
        }

        // 이메일 중복 검사
        if(request.email()!=null){
            boolean exitstence = customerRepository.existsByEmailAndIdNot(request.email(), id);
            if(exitstence){
                throw new CommonException(CommonError.DUPLICATE_EMAIL);
            }
        }
        customer.updateCustomer(request.name(), request.email(), request.phone());
        return customerRepository.findByIdWithOrderStats(id);
    }

    // 상태 업데이트
    @Transactional
    public GetCustomerResponse updateStatus(Long id, UpdateStatusRequest request) {
        CustomerStatus status = request.toCustomerStatus();

        Customer customer = customerRepository.findByIdAndDeletedFalse(id).orElseThrow(
                ()->new CommonException(CommonError.CUSTOMER_NOT_FOUND));
        // 만약 기존 상태와 같은 상태로 업데이트 요청이 들어오면 쿼리문은 날리지 않는것이..?
        customer.updateStatus(status);
        return customerRepository.findByIdWithOrderStats(id);
    }

    @Transactional
    public void deleteCustomer(Long id) {
        Customer customer = customerRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new CommonException(CommonError.CUSTOMER_NOT_FOUND));

        customerRepository.delete(customer);
    }
}









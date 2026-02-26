package com.ecommerce.backoffice.domain.order.controller;

import com.ecommerce.backoffice.domain.order.dto.request.CancelOrderRequest;
import com.ecommerce.backoffice.domain.order.dto.request.UpdateStatusOrderRequest;
import com.ecommerce.backoffice.domain.order.dto.response.CancelOrderResponse;
import com.ecommerce.backoffice.domain.order.dto.response.UpdateStatusOrderResponse;
import com.ecommerce.backoffice.domain.order.service.OrderStatusService;
import com.ecommerce.backoffice.global.common.ApiResponse;
import com.ecommerce.backoffice.global.error.CommonError;
import com.ecommerce.backoffice.global.error.CommonException;
import com.ecommerce.backoffice.global.security.UserDetailsImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class OrderStatusController {

    private final OrderStatusService orderStatusService;

    /**
     * 주문 상태 수정
     */
    @PreAuthorize("hasAnyAuthority('SUPER_ADMIN','OPERATION_ADMIN')")
    @PatchMapping("/orders/{id}/status")
    public ResponseEntity<ApiResponse<UpdateStatusOrderResponse>> updateStutusOrder(
            @PathVariable Long id,
            @Valid @RequestBody UpdateStatusOrderRequest request
    ) {
        return ResponseEntity.status(HttpStatus.OK).body(
                ApiResponse.success(orderStatusService.updateStatusOrder(id, request)));
    }

    /**
     * 주문 취소
     */
    @PreAuthorize("hasAnyAuthority('SUPER_ADMIN','CS_ADMIN')")
    @PatchMapping("/orders/{id}/cancel")
    public ResponseEntity<ApiResponse<CancelOrderResponse>> cancelOrder(
            @PathVariable Long id,
            @Valid @RequestBody CancelOrderRequest request
    ) {
        return ResponseEntity.status(HttpStatus.OK).body(
                ApiResponse.success(orderStatusService.cancelOrder(id, request)));
    }
}

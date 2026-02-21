package com.ecommerce.backoffice.domain.order.controller;

import com.ecommerce.backoffice.domain.order.dto.request.CancelOrderRequest;
import com.ecommerce.backoffice.domain.order.dto.request.UpdateStatusOrderRequest;
import com.ecommerce.backoffice.domain.order.dto.response.CancelOrderResponse;
import com.ecommerce.backoffice.domain.order.dto.response.UpdateStatusOrderResponse;
import com.ecommerce.backoffice.domain.order.service.OrderStatusService;
import com.ecommerce.backoffice.global.common.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class OrderStatusController {

    private final OrderStatusService orderStatusService;

    /**
     * 주문 상태 수정
     */
    @PatchMapping("/orders/{id}/status")
    public ResponseEntity<ApiResponse<UpdateStatusOrderResponse>> updateStutusOrder(
            @PathVariable Long id,
            @RequestBody UpdateStatusOrderRequest request) {
        return ResponseEntity.status(HttpStatus.OK).body(
                ApiResponse.success(orderStatusService.updateStatusOrder(id, request)));
    }

    /**
     * 주문 취소
     */
    @PatchMapping("/orders/{id}/cancel")
    public ResponseEntity<ApiResponse<CancelOrderResponse>> cancelOrder(
            @PathVariable Long id,
            @RequestBody CancelOrderRequest request
    ) {
        return ResponseEntity.status(HttpStatus.OK).body(
                ApiResponse.success(orderStatusService.cancelOrder(id, request)));
    }
}

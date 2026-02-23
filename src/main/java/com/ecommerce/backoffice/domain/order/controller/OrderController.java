package com.ecommerce.backoffice.domain.order.controller;

import com.ecommerce.backoffice.domain.order.dto.request.CreateOrderRequest;
import com.ecommerce.backoffice.domain.order.dto.request.OrderSearchRequest;
import com.ecommerce.backoffice.domain.order.dto.response.CreateOrderResponse;
import com.ecommerce.backoffice.domain.order.dto.response.GetOrderResponse;
import com.ecommerce.backoffice.domain.order.dto.response.GetOrdersResponse;
import com.ecommerce.backoffice.domain.order.service.OrderService;
import com.ecommerce.backoffice.global.common.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;

    // 주문 POST
    @PostMapping
    public ResponseEntity<ApiResponse<CreateOrderResponse>> saveOrder(
            @Valid @RequestBody CreateOrderRequest request,
            HttpServletRequest sessionRequest
    ) {
        CreateOrderResponse response = orderService.save(request, sessionRequest);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.success(response));
    }

    //주문 목록 조회 GET
    @GetMapping
    public ResponseEntity<ApiResponse<Page<GetOrdersResponse>>> findOrders(
            @RequestParam(defaultValue = "") String keyword,
            @RequestParam(required = false) String status,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String direction
    ) {
        OrderSearchRequest request = OrderSearchRequest.builder()
                .keyword(keyword)
                .status(status)
                .sortBy(sortBy)
                .direction(direction)
                .build();

        Page<GetOrdersResponse> result = orderService.findOrders(request, page, size);

        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success(result));

    }

    // 주문 상세 조회 GET
    @GetMapping("/{orderId}")
    public ResponseEntity<ApiResponse<GetOrderResponse>> getOne(
            @PathVariable Long orderId
    ) {
        GetOrderResponse response = orderService.getOne(orderId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.success(response));
    }
}

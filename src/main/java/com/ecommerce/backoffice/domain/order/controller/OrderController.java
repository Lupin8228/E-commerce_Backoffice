package com.ecommerce.backoffice.domain.order.controller;

import com.ecommerce.backoffice.domain.order.dto.request.CreateOrderRequest;
import com.ecommerce.backoffice.domain.order.dto.response.CreateOrderResponse;
import com.ecommerce.backoffice.domain.order.service.OrderService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;

    // 주문 POST
    @PostMapping
    public ResponseEntity<CreateOrderResponse> saveOrder(
            @RequestBody CreateOrderRequest request,
            HttpSession session
    ) {
        return ResponseEntity.ok().body(orderService.save(session, request));
    }
}

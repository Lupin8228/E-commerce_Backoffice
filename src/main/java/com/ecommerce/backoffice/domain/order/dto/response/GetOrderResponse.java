package com.ecommerce.backoffice.domain.order.dto.response;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record GetOrderResponse(

        String orderNumber,
        String customer,
        String email,
        String product,
        Integer quantity,
        Long totalPrice,
        LocalDateTime createdAt,
        String status,
        String admin,
        String adminEmail,
        String adminGrade
) {
}

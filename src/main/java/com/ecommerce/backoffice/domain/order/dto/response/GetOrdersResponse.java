package com.ecommerce.backoffice.domain.order.dto.response;

import com.ecommerce.backoffice.domain.order.entity.Order;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record GetOrdersResponse(
        Long id,
        String orderNumber,
        String customer,
        String product,
        Integer quantity,
        Long totalPrice,
        LocalDateTime createdAt,
        String status,
        String admin
) {
    public static GetOrdersResponse of(Order order) {
        return GetOrdersResponse.builder()
                .id(order.getId())
                .orderNumber(order.getOrderNumber())
                .customer(order.getCustomer().getName())
                .product(order.getProduct().getName())
                .quantity(order.getQuantity())
                .totalPrice(order.getTotalPrice())
                .createdAt(order.getCreatedAt())
                .status(order.getStatus().name())
                .admin(order.getAdmin().getEmail())
                .build();
    }
}

package com.ecommerce.backoffice.domain.order.dto.response;

import com.ecommerce.backoffice.domain.order.entity.Order;
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
    public static GetOrderResponse of(Order order) {
        String adminEmail = null;
        if(order.getAdmin() != null) {
            adminEmail = order.getAdmin().getEmail();
        }

        String customerName = null;
        if(order.getCustomer() != null) {
            customerName = order.getCustomer().getName();
        }

        String productName = null;
        if(order.getProduct() != null) {
            productName = order.getProduct().getName();
        }

        return GetOrderResponse.builder()
                .orderNumber(order.getOrderNumber())
                .customer(customerName)
                .product(productName)
                .quantity(order.getQuantity())
                .totalPrice(order.getTotalPrice())
                .createdAt(order.getCreatedAt())
                .status(order.getStatus().name())
                .admin(adminEmail)
                .build();
    }
}

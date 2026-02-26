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

        return GetOrdersResponse.builder()
                .id(order.getId())
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

package com.ecommerce.backoffice.domain.order.dto.response;

import com.ecommerce.backoffice.domain.customer.entity.Customer;
import com.ecommerce.backoffice.domain.product.entity.Product;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record CreateOrderResponse (
        String orderNumber,
        Customer customer,
        Product product,
        String message

){
    public static CreateOrderResponse of(String orderNumber, Customer customer, Product product) {
        return CreateOrderResponse.builder()
                .orderNumber(orderNumber)
                .customer(customer)
                .product(product)
                .message(orderNumber + "의 주문이 접수되었습니다.")
                .build();
    }
}

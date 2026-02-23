package com.ecommerce.backoffice.domain.review.dto.response;

import com.ecommerce.backoffice.domain.customer.entity.Customer;
import com.ecommerce.backoffice.domain.product.entity.Product;
import com.ecommerce.backoffice.domain.review.entity.Review;

import java.time.LocalDateTime;

public record GetDetailReviewResponse(
        String productName,
        String customerName,
        String customerEmail,
        LocalDateTime createdAt,
        int rating,
        String description
) {
    public static GetDetailReviewResponse from(Review review, Product product, Customer customer) {
        return new GetDetailReviewResponse(
                product.getName(),
                customer.getName(),
                customer.getEmail(),
                review.getCreatedAt(),
                review.getRating(),
                review.getDescription()
        );
    }
}

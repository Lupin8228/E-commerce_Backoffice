package com.ecommerce.backoffice.domain.review.dto.response;

import com.ecommerce.backoffice.domain.customer.entity.Customer;
import com.ecommerce.backoffice.domain.product.entity.Product;
import com.ecommerce.backoffice.domain.review.entity.Review;

import java.time.LocalDateTime;

public record GetReviewResponse(
        Long id,
        Long productId,
        String customerName,
        String productName,
        Integer rating,
        String description,
        LocalDateTime createdAt
) {
    public static GetReviewResponse from(Review review, Product product, Customer customer) {
        return new GetReviewResponse(
                review.getId(),
                product.getId(),
                customer.getName(),
                product.getName(),
                review.getRating(),
                review.getDescription(),
                review.getCreatedAt()
        );
    }
}

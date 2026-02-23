package com.ecommerce.backoffice.domain.product.dto.response;

import com.ecommerce.backoffice.domain.review.entity.Review;

import java.time.LocalDateTime;

//최신 리뷰 3개
public record LatestReviewResponse(
        String customerName,
        int rating,
        String description,
        LocalDateTime createdAt
) {
    public static LatestReviewResponse from(Review review) {
        return new LatestReviewResponse(
                review.getCustomer().getName(),
                review.getRating(),
                review.getDescription(),
                review.getCreatedAt()
        );
    }
}

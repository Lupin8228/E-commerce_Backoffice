package com.ecommerce.backoffice.domain.review.dto.response;

import com.ecommerce.backoffice.domain.review.entity.Review;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record CreateReviewResponse(
        Long id,
        int rating,
        String description,
        LocalDateTime createdAt
) {
    public static CreateReviewResponse from(Review review){
        return CreateReviewResponse.builder()
                .id(review.getId())
                .rating(review.getRating())
                .description(review.getDescription())
                .createdAt(review.getCreatedAt())
                .build();
    }
}

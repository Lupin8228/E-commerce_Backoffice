package com.ecommerce.backoffice.domain.dashboard.dto.summary;

import lombok.Builder;

@Builder
public record ReviewSummaryDto(
        Long totalReviews,
        Double avgRating
) {

}

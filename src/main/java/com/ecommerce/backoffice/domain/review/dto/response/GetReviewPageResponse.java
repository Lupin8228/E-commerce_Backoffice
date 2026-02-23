package com.ecommerce.backoffice.domain.review.dto.response;

import org.springframework.data.domain.Page;

import java.util.List;

public record GetReviewPageResponse(
        List<GetReviewResponse> reviews,
        long totalElements,
        int totalPages,
        int currentPage,
        int size
) {
    public static GetReviewPageResponse from(Page<GetReviewResponse> page) {
        return new GetReviewPageResponse(
                page.getContent(),
                page.getTotalElements(),
                page.getTotalPages(),
                page.getNumber()+1,
                page.getSize()
        );
    }
}

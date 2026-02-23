package com.ecommerce.backoffice.domain.product.dto.response;

import java.util.List;
import java.util.Map;

//상품 상세 페이지용 최신 리뷰 정보
public record ProductReviewResponse(
        double avgRating, //평균 평점 (소수점 1자리)
        long totalReviewCount, //전체 리뷰 개수
        Map<Integer, Long> ratingCounts, //별점별 개수 (1:n개, 2:n개 ..)
        List<LatestReviewResponse> latestReviews //최신 리뷰 3개
) {
}

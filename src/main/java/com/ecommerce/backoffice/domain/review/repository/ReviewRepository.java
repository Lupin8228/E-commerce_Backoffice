package com.ecommerce.backoffice.domain.review.repository;

import com.ecommerce.backoffice.domain.dashboard.dto.chart.DashboardChartItem;
import com.ecommerce.backoffice.domain.dashboard.dto.summary.ReviewSummaryDto;
import com.ecommerce.backoffice.domain.review.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Integer> {
    @Query("""
        SELECT new com.ecommerce.backoffice.domain.dashboard.dto.summary.ReviewSummaryDto(
            COUNT(r),
            CAST(COALESCE(AVG(r.rating), 0) as double)
        )
        FROM Review r
        WHERE r.deleted = false
    """)
    ReviewSummaryDto getReviewSummary();

    @Query("""
        SELECT new com.ecommerce.backoffice.domain.dashboard.dto.chart.DashboardChartItem(
            CAST(r.rating as string),
            COUNT(r)
        )
        FROM Review r
        WHERE r.deleted = false
        GROUP BY r.rating
        ORDER BY r.rating
        """)
    List<DashboardChartItem> countRatingDistribution();
}

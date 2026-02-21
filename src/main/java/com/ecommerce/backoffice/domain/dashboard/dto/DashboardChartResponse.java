package com.ecommerce.backoffice.domain.dashboard.dto;

import lombok.Builder;

import java.util.List;

@Builder
public record DashboardChartResponse(
        List<DashboardChartItem> ratingDistribution,
        List<DashboardChartItem> customerStatusDistribution,
        List<DashboardChartItem> categoryDistribution
) {

    public static DashboardChartResponse of(
            List<DashboardChartItem> ratingDist,
            List<DashboardChartItem> customerDist,
            List<DashboardChartItem> categoryDist) {
        return DashboardChartResponse.builder()
                .ratingDistribution(ratingDist)
                .customerStatusDistribution(customerDist)
                .categoryDistribution(categoryDist)
                .build();
    }
}

package com.ecommerce.backoffice.domain.dashboard.dto.summary;

import lombok.Builder;

@Builder
public record DashboardSummaryResponse(
        Long totalAdmins,
        Long activeAdmins,
        Long totalCustomers,
        Long activeCustomers,
        Long totalProducts,
        Long lowStockProducts,
        Long totalOrders,
        Long todayOrders,
        Long totalReviews,
        double avgRating
) {
    public static DashboardSummaryResponse of(
            Long totalAdmins,
            Long activeAdmins,
            Long totalCustomers,
            Long activeCustomers,
            Long totalProducts,
            Long lowStockProducts,
            Long totalOrders,
            Long todayOrders,
            Long totalReviews,
            Double avgRating) {
        return DashboardSummaryResponse.builder()
                .totalAdmins(totalAdmins)
                .activeAdmins(activeAdmins)
                .totalCustomers(totalCustomers)
                .activeCustomers(activeCustomers)
                .totalProducts(totalProducts)
                .lowStockProducts(lowStockProducts)
                .totalOrders(totalOrders)
                .todayOrders(todayOrders)
                .totalReviews(totalReviews)
                .avgRating(avgRating)
                .build();
    }
}

package com.ecommerce.backoffice.domain.dashboard.dto;

import lombok.Builder;

@Builder
public record DashboardWidgetResponse(
        Long totalSales,
        Long todaySales,
        Long readyOrders,
        Long shippingOrders,
        Long deliveredOrders,
        Long lowStockProducts,
        Long soldOutProducts
) {

    public static DashboardWidgetResponse of(
            Long totalSales,
            Long todaySales,
            Long ready,
            Long shipping,
            Long delivered,
            Long lowStock,
            Long soldOut) {
        return DashboardWidgetResponse.builder()
                .totalSales(totalSales)
                .todaySales(todaySales)
                .readyOrders(ready)
                .shippingOrders(shipping)
                .deliveredOrders(delivered)
                .lowStockProducts(lowStock)
                .soldOutProducts(soldOut)
                .build();
    }
}

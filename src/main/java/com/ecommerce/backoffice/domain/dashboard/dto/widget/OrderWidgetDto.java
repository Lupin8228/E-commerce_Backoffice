package com.ecommerce.backoffice.domain.dashboard.dto.widget;

import lombok.Builder;

@Builder
public record OrderWidgetDto(
        Long totalSales,
        Long todaySales,
        Long preparingCount,
        Long shippingCount,
        Long deliveredCount
) {

}

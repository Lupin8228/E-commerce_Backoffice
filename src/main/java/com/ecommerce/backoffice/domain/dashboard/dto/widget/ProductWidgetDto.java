package com.ecommerce.backoffice.domain.dashboard.dto.widget;

import lombok.Builder;

@Builder
public record ProductWidgetDto(
        Long lowStockCount,
        Long soldOutCount
) {

}

package com.ecommerce.backoffice.domain.dashboard.dto;

import lombok.Builder;

@Builder
public record DashboardChartItem (
    String label,
    Long value
) {

}

package com.ecommerce.backoffice.domain.dashboard.dto.chart;

import lombok.Builder;

@Builder
public record DashboardChartItem (
    String label,
    Long value
) {

}

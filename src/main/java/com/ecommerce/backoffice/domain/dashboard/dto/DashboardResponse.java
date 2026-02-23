package com.ecommerce.backoffice.domain.dashboard.dto;

import com.ecommerce.backoffice.domain.dashboard.dto.chart.DashboardChartResponse;
import com.ecommerce.backoffice.domain.dashboard.dto.recentOrder.RecentOrderResponse;
import com.ecommerce.backoffice.domain.dashboard.dto.summary.DashboardSummaryResponse;
import com.ecommerce.backoffice.domain.dashboard.dto.widget.DashboardWidgetResponse;
import lombok.Builder;

import java.util.List;

@Builder
public record DashboardResponse (
        DashboardSummaryResponse summary,
        DashboardWidgetResponse widgets,
        DashboardChartResponse charts,
        List<RecentOrderResponse> recentOrders
) {
    public static DashboardResponse of(
            DashboardSummaryResponse summary,
            DashboardWidgetResponse widgets,
            DashboardChartResponse charts,
            List<RecentOrderResponse> recentOrders) {
        return DashboardResponse.builder()
                .summary(summary)
                .widgets(widgets)
                .charts(charts)
                .recentOrders(recentOrders)
                .build();
    }
}
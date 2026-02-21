package com.ecommerce.backoffice.domain.product.repository;

import com.ecommerce.backoffice.domain.dashboard.dto.chart.DashboardChartItem;
import com.ecommerce.backoffice.domain.dashboard.dto.widget.ProductWidgetDto;
import com.ecommerce.backoffice.domain.product.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
    Long countByDeletedFalse();

    Long countByStockLessThanEqualAndDeletedFalse(int i);

    @Query("""
        SELECT new com.ecommerce.backoffice.domain.dashboard.dto.widget.ProductWidgetDto(
            SUM(CASE WHEN p.deleted = false AND p.stock <= 5 THEN 1 ELSE 0 END),
            SUM(CASE WHEN p.deleted = false AND p.stock <= 0 THEN 1 ELSE 0 END)
        )
        FROM Product p
    """)
    ProductWidgetDto getProdectWidget();

    @Query("""
        SELECT new com.ecommerce.backoffice.domain.dashboard.dto.chart.DashboardChartItem(
            CAST(p.category as string),
            COUNT(p)
        )
        FROM Product p
        WHERE p.deleted = false
        GROUP BY p.category
        ORDER BY p.category
        """)
    List<DashboardChartItem> countCategoryDistribution();
}

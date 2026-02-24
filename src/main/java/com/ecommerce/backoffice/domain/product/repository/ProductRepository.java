package com.ecommerce.backoffice.domain.product.repository;

import com.ecommerce.backoffice.domain.product.dto.response.GetAllProductResponse;
import com.ecommerce.backoffice.domain.dashboard.dto.chart.DashboardChartItem;
import com.ecommerce.backoffice.domain.dashboard.dto.widget.ProductWidgetDto;
import com.ecommerce.backoffice.domain.product.entity.Product;
import com.ecommerce.backoffice.domain.product.enums.ProductCategory;
import com.ecommerce.backoffice.domain.product.enums.ProductStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
    @Query("""
        SELECT new com.ecommerce.backoffice.domain.product.dto.response.GetAllProductResponse(
            p.id, p.name, p.category, p.price, p.stock, p.status, p.createdAt, a.name
        )
        FROM Product p
        LEFT JOIN p.createdBy a
        WHERE (:name IS NULL OR p.name LIKE %:name%)
        AND (:category IS NULL OR p.category = :category)
        AND (:status IS NULL OR p.status = :status)
    """)
    Page<GetAllProductResponse> findAllProducts(
            @Param("name") String name,
            @Param("category") ProductCategory category,
            @Param("status") ProductStatus status,
            Pageable pageable
    );
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

package com.ecommerce.backoffice.domain.order.repository;

import com.ecommerce.backoffice.domain.dashboard.dto.recentOrder.RecentOrderResponse;
import com.ecommerce.backoffice.domain.dashboard.dto.summary.OrderSummaryDto;
import com.ecommerce.backoffice.domain.dashboard.dto.widget.OrderWidgetDto;
import com.ecommerce.backoffice.domain.order.entity.Order;
import com.ecommerce.backoffice.domain.order.enums.OrderStatus;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long>, OrderRepositoryCustom {
    List<Order> findByCustomerId(Long customerId);

    @Query("""
        SELECT new com.ecommerce.backoffice.domain.dashboard.dto.summary.OrderSummaryDto(
            COUNT(o),
            CAST(COALESCE(SUM(CASE 
                    WHEN o.createdAt BETWEEN :start AND :end THEN 1
                    ELSE 0
                END),0) as long)
        )
        FROM Order o
        WHERE o.status <> :cancelStatus
    """)
    OrderSummaryDto getOrderSummary(
            @Param("cancelStatus") OrderStatus orderStatus,
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end);

    @Query("""
        SELECT new com.ecommerce.backoffice.domain.dashboard.dto.widget.OrderWidgetDto(
        CAST(COALESCE(SUM(CASE WHEN o.status <> :cancelStatus THEN o.totalPrice ELSE 0 END),0) as long),
        CAST(COALESCE(SUM(CASE
            WHEN o.status <> :cancelStatus
             AND o.createdAt BETWEEN :start AND :end
            THEN o.totalPrice ELSE 0 END),0) as long),
        SUM(CASE WHEN o.status = 'PREPARING' THEN 1 ELSE 0 END),
        SUM(CASE WHEN o.status = 'SHIPPING' THEN 1 ELSE 0 END),
        SUM(CASE WHEN o.status = 'DELIVERED' THEN 1 ELSE 0 END)
    )
    FROM Order o
    """)
    OrderWidgetDto getOrderWidget(
            @Param("cancelStatus") OrderStatus orderStatus,
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end);

    @Query("""
        SELECT new com.ecommerce.backoffice.domain.dashboard.dto.recentOrder.RecentOrderResponse(
            o.orderNumber,
            c.name,
            p.name,
            o.totalPrice,
            o.status
        )
        FROM Order o
        JOIN o.customer c
        JOIN o.product p
        WHERE o.deleted = false
        ORDER BY o.createdAt DESC
        LIMIT 10
        """)
    List<RecentOrderResponse> findRecentOrders();
}

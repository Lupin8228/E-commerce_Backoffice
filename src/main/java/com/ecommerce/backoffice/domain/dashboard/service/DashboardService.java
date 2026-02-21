package com.ecommerce.backoffice.domain.dashboard.service;

import com.ecommerce.backoffice.domain.admin.repository.AdminRepository;
import com.ecommerce.backoffice.domain.customer.enums.CustomerStatus;
import com.ecommerce.backoffice.domain.customer.repository.CustomerRepository;
import com.ecommerce.backoffice.domain.dashboard.dto.*;
import com.ecommerce.backoffice.domain.order.enums.OrderStatus;
import com.ecommerce.backoffice.domain.order.repository.OrderRepository;
import com.ecommerce.backoffice.domain.product.repository.ProductRepository;
import com.ecommerce.backoffice.domain.review.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DashboardService {

    private final AdminRepository adminRepository;
    private final CustomerRepository customerRepository;
    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;
    private final ReviewRepository reviewRepository;

    final static LocalDate today = LocalDate.now();
    final static LocalDateTime start = today.atStartOfDay();
    final static LocalDateTime end = today.atTime(LocalTime.MAX);

    /**
     * 대시보드 전체 조회
     */
    public DashboardResponse getDashboard() {
        // Summary 통계
        //DashboardSummaryResponse summary = getSummary();
        // Widgets 데이터
        //DashboardWidgetResponse widgets = getWidgets();
        // Charts 데이터
        //DashboardChartResponse charts = getCharts();
        // 최근 주문 목록
        //List<RecentOrderResponse> recentOrders = getRecentOrders();

        //return DashboardResponse.of(summary, widgets, charts, recentOrders);
        return null;
    }

    /**
     * Summary 통계
     */
    /*private DashboardSummaryResponse getSummary() {
        // deleted가 null인 전체 admin 수
        Long totalAdmins = adminRepository.countByDeletedIsNull();
        // deleted가 null이고 status가 APPROVED(활성)인 admin 수
        Long activeAdmins = adminRepository.countByStatusAndDeletedIsNull(AdminStatus.APPROVED);

        // deleted가 null인 전체 customer 수
        Long totalCustomers = customerRepository.countByDeletedIsNull();
        // deleted가 null이고 status가 ACTIVE(활성)인 customer 수
        Long activeCustomers = customerRepository.countByStatusAndDeletedIsNull(CustomerStatus.ACTIVE);

        // deleted가 null인 전체 product 수
        Long totalProducts = productRepository.countByDeletedIsNull();
        // deleted가 null이고 stock이 5개 이하인 product 수
        Long lowStockProducts = productRepository.countByStockLessThanEqualAndDeletedIsNull(5);

        // status가 CANCELLED(취소됨)이 아닌 전체 order 수
        Long totalOrders = orderRepository.countByStatusNot(OrderStatus.CANCELLED);
        // status가 CANCELLED(취소됨)이 아니면서 오늘 날짜의 order 수
        Long todayOrders = orderRepository.countByStatusNotAndCreatedAtBetween(
                OrderStatus.CANCELLED,
                start,
                end);

        // deleted가 null인 전체 review 수
        Long totalReviews = reviewRepository.countByDeletedIsNull();
        // deleted가 null인 전체 review 수의 평균
        Double avgRating = reviewRepository.avgRating();

        return DashboardSummaryResponse.of(
                totalAdmins, activeAdmins,
                totalCustomers, activeCustomers,
                totalProducts, lowStockProducts,
                totalOrders, todayOrders,
                totalReviews, avgRating
        );
    }*/

    /**
     * Widgets 데이터
     */
    /*private DashboardWidgetResponse getWidgets() {

        // status가 CANCELLED(취소됨)이 아닌 order의 총 매출
        Long totalSales = orderRepository.sumTotalPrice(OrderStatus.CANCELLED);

        // status가 CANCELLED(취소됨)이 아닌 order의 오늘 매출
        Long todaySales = orderRepository.sumTodaySales(
                OrderStatus.CANCELLED,
                start,
                end);

        // status가 준비중, 배송중, 배송완료인 order 수
        Long ready = orderRepository.countByStatus(OrderStatus.PREPARING);
        Long shipping = orderRepository.countByStatus(OrderStatus.SHIPPING);
        Long delivered = orderRepository.countByStatus(OrderStatus.DELIVERED);

        // deleted가 null이고 stock이 5개 이하인 product 수
        Long lowStock = productRepository.countByStockLessThanEqualAndDeletedIsNull(5);
        // deleted가 null이고 stock이 0개 이하인 product 수
        Long soldOut = productRepository.countByStockLessThanEqualAndDeletedIsNull(0);

        return DashboardWidgetResponse.of(
                totalSales, todaySales,
                ready, shipping, delivered,
                lowStock, soldOut
        );
    }*/

    /**
     * Charts 데이터
     */
    /*private DashboardChartResponse getCharts() {

        List<DashboardChartItem> ratingDist = reviewRepository.countRatingDistribution();
        List<DashboardChartItem> customerDist = customerRepository.countStatusDistribution();
        List<DashboardChartItem> categoryDist = productRepository.countCategoryDistribution();

        return DashboardChartResponse.of(
                ratingDist,
                customerDist,
                categoryDist
        );
    }*/

    /**
     * 최근 주문 목록
     */
    /*private List<RecentOrderResponse> getRecentOrders() {

        return orderRepository.findTop10ByOrderByCreatedAtDesc();
    }*/
}

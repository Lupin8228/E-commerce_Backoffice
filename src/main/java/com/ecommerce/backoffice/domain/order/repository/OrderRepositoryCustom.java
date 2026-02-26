package com.ecommerce.backoffice.domain.order.repository;

import com.ecommerce.backoffice.domain.order.dto.request.OrderSearchRequest;
import com.ecommerce.backoffice.domain.order.entity.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface OrderRepositoryCustom {
    Page<Order> searchOrders(OrderSearchRequest request, Pageable pageable);
}

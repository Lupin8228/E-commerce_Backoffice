package com.ecommerce.backoffice.domain.order.repository;

import com.ecommerce.backoffice.domain.order.dto.request.OrderSearchRequest;
import com.ecommerce.backoffice.domain.order.entity.Order;
import com.ecommerce.backoffice.domain.order.entity.QOrder;
import com.ecommerce.backoffice.domain.order.enums.OrderStatus;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static com.ecommerce.backoffice.domain.order.entity.QOrder.order;

@RequiredArgsConstructor
public class OrderRepositoryImpl implements OrderRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<Order> searchOrders(OrderSearchRequest request, Pageable pageable) {
        QOrder orders = order;

        BooleanExpression keywordCond = null;
        if(request.keyword() != null) {
            keywordCond = orders.orderNumber.contains(request.keyword())
                    .or(orders.customer.name.contains(request.keyword()));
        }

        BooleanExpression statusCond = null;
        if(request.status() != null) {
            statusCond = orders.status.eq(OrderStatus.valueOf(request.status()));
        }

        List<Order> content = queryFactory
                .selectFrom(order)
                .where(keywordCond, statusCond)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        return new PageImpl<>(content, pageable, content.size());
    }
}

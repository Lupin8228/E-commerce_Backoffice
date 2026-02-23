package com.ecommerce.backoffice.domain.customer.repository;

import com.ecommerce.backoffice.domain.customer.dto.response.GetCustomerResponse;
import com.ecommerce.backoffice.domain.customer.entity.Customer;
import com.ecommerce.backoffice.domain.customer.entity.QCustomer;
import com.ecommerce.backoffice.domain.order.entity.QOrder;
import com.ecommerce.backoffice.domain.order.enums.OrderStatus;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;

@RequiredArgsConstructor
public class CustomerRepositoryImpl implements CustomerRepositoryCustom{
    private final JPAQueryFactory queryFactory;

//    @Override
//    public Page<Customer> search(String search, Pageable pageable) {
//
//        QCustomer customer = QCustomer.customer;
//
//        List<Customer> content = queryFactory
//                .selectFrom(customer)
//                .where(searchCondition(search),customer.deleted.eq(false))
//                .offset(pageable.getOffset())
//                .limit(pageable.getPageSize())
//                .orderBy(getOrderSpecifiers(pageable))
//                .fetch();
//
//
//        long total = queryFactory
//                .select(customer.count())
//                .from(customer)
//                .where(searchCondition(search))
//                .fetchOne();
//
//        return new PageImpl<>(content, pageable, total);
//    }

    @Override
    public GetCustomerResponse findByIdWithOrderStats(Long id) {

        QCustomer customer = QCustomer.customer;
        QOrder order = QOrder.order;

        return queryFactory
                .select(Projections.constructor(
                        GetCustomerResponse.class,
                        customer.id,
                        customer.name,
                        customer.email,
                        customer.phone,
                        customer.status,
                        customer.createdAt,
                        order.countDistinct(),
                        order.totalPrice.sum().coalesce(0L)
                ))
                .from(customer)
                .leftJoin(order).on(order.customer.id.eq(customer.id))
                .where(customer.id.eq(id))
                .groupBy(customer.id)
                .fetchOne();
    }

    @Override
    public Page<GetCustomerResponse> searchWithOrderStats(String search, Pageable pageable) {

        QCustomer customer = QCustomer.customer;
        QOrder order = QOrder.order;

        // 1️⃣ 실제 데이터 조회
        List<GetCustomerResponse> content = queryFactory
                .select(Projections.constructor(
                        GetCustomerResponse.class,
                        customer.id,
                        customer.name,
                        customer.email,
                        customer.phone,
                        customer.status,
                        customer.createdAt,
                        order.countDistinct(),   // ✅ 총 주문 수
                        order.totalPrice.sum().coalesce(0L) // ✅ 총 주문 금액
                ))
                .from(customer)
                .leftJoin(order)    // 고객에게 주문이 없어도 고객 조회
                .on(order.customer.eq(customer)
                        .and(order.status.ne(OrderStatus.CANCELLED))) // 취소 주문 제외
                .where(
                        searchCondition(search),
                        customer.deleted.eq(false) // soft delete 제외
                )
                .groupBy(customer.id)   // 고객별로 집계

                // 페이징 처리
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(getOrderSpecifiers(pageable))
                .fetch();


        // 2️⃣ 전체 개수 조회 (페이징용)
        Long total = queryFactory
                .select(customer.count())
                .from(customer)
                .where(
                        searchCondition(search),
                        customer.deleted.eq(false)
                )
                .fetchOne();

        return new PageImpl<>(content, pageable, total);
    }




    private BooleanExpression searchCondition(String search) {
        if (search == null || search.isBlank()) {
            return null;
        }

        QCustomer customer = QCustomer.customer;

        return customer.name.containsIgnoreCase(search)
                .or(customer.email.containsIgnoreCase(search));
    }

    private OrderSpecifier<?>[] getOrderSpecifiers(Pageable pageable) {

        QCustomer customer = QCustomer.customer;

        return pageable.getSort().stream()
                .map(order -> {
                    PathBuilder<Customer> pathBuilder =
                            new PathBuilder<>(Customer.class, "customer");

                    return new OrderSpecifier(
                            order.isAscending() ? Order.ASC : Order.DESC,
                            pathBuilder.get(order.getProperty())
                    );
                })
                .toArray(OrderSpecifier[]::new);
    }
}

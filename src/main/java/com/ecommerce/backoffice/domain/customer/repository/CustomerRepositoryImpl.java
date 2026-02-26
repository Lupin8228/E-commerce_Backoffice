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
                .leftJoin(order).on(order.customer.id.eq(customer.id)
                        .and(order.status.ne(OrderStatus.CANCELLED)))
                .where(customer.id.eq(id))
                .groupBy(customer.id)
                .fetchOne();
    }

    @Override
    public Page<GetCustomerResponse> searchWithOrderStats(String search, Pageable pageable) {

        QCustomer customer = QCustomer.customer;
        QOrder order = QOrder.order;

        // 실제 데이터 조회
        List<GetCustomerResponse> content = queryFactory
                .select(Projections.constructor(
                        GetCustomerResponse.class,
                        customer.id,
                        customer.name,
                        customer.email,
                        customer.phone,
                        customer.status,
                        customer.createdAt,
                        order.countDistinct(),   // ✅ 총 주문 수, 확장성 고려
                        order.totalPrice.sum().coalesce(0L) // ✅ 총 주문 금액
                ))
                .from(customer)
                .leftJoin(order)    // 고객에게 주문이 없어도 고객 조회
                .on(order.customer.eq(customer)
                        .and(order.status.ne(OrderStatus.CANCELLED))) // 취소 주문 제외
                .where(
                        searchCondition(search)
                )
                .groupBy(customer.id)   // 고객별로 집계

                // 페이징 처리
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(getOrderSpecifiers(pageable))
                .fetch();


        // 전체 개수 조회 (페이징용)
        Long total = queryFactory
                .select(customer.count())
                .from(customer)
                .where(
                        searchCondition(search)
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
    // Pageable에 있는 정렬 조건(?page=0&size=10&sort=name,desc)을 QueryDSL에서 사용하는 OrderSpecifier로 변환하는 메서드
    private OrderSpecifier<?>[] getOrderSpecifiers(Pageable pageable) {

        // 필드가 고정되어 있어야 사용 가능하므로 문자열 기반 접근이 가능한 PathBuilder 사용
//        QCustomer customer = QCustomer.customer;

        return pageable.getSort().stream()
                .map(order -> {
                    // Customer 클래스를 "customer"라는 별명(alias)로 사용
                    // PathBuilder : 문자열로 필드명을 받아서 QueryDSL Path 객체로 바꿔주는 도구
                    PathBuilder<Customer> pathBuilder =
                            new PathBuilder<>(Customer.class, "customer");

                    return new OrderSpecifier(  // 정렬 객체
                            order.isAscending() ? Order.ASC : Order.DESC,
                            // "정렬 기준 "name" 문자열 -> customer.name 이라는 Path 객체 생성
                            pathBuilder.get(order.getProperty())
                    ); // 생성된 Path 뒤에 ASC/DESC를 붙인 OrderSpecifier를 QueryDSL의 orderBy로 전달
                })
                .toArray(OrderSpecifier[]::new);
    }
}

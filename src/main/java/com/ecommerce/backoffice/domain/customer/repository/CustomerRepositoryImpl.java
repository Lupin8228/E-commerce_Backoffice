package com.ecommerce.backoffice.domain.customer.repository;

import com.ecommerce.backoffice.domain.customer.entity.Customer;
import com.ecommerce.backoffice.domain.customer.entity.QCustomer;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
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
    public Page<Customer> search(String search, Pageable pageable) {

        QCustomer customer = QCustomer.customer;

        List<Customer> content = queryFactory
                .selectFrom(customer)
                .where(searchCondition(search),customer.deleted.eq(false))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(getOrderSpecifiers(pageable))
                .fetch();


        long total = queryFactory
                .select(customer.count())
                .from(customer)
                .where(searchCondition(search))
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

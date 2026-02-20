package com.ecommerce.backoffice.domain.order.entity;

import com.ecommerce.backoffice.domain.admin.entity.Admin;
import com.ecommerce.backoffice.domain.customer.entity.Customer;
import com.ecommerce.backoffice.domain.order.enums.OrderStatus;
import com.ecommerce.backoffice.domain.product.entity.Product;
import com.ecommerce.backoffice.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Getter
@Entity
@Table(name = "orders")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class Order extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String orderNumber;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrderStatus status;

    @Column(nullable = false)
    private Long totalPrice;

    @Column(nullable = false)
    private int quantity;

    @Column(length = 255)
    private String cancelReason;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "admin_id")
    private Admin admin;

    @Builder
    public Order(String orderNumber, OrderStatus status, Long totalPrice, int quantity,
                 Customer customer, Product product, Admin admin) {
        this.orderNumber = orderNumber;
        this.status = status;
        this.totalPrice = totalPrice;
        this.quantity = quantity;
        this.customer = customer;
        this.product = product;
        this.admin = admin;
    }

    public void updateStatus(OrderStatus status) {
        this.status = status;
    }

    public void updateStatusAndCancelReason(OrderStatus status, String cancelReason) {
        this.status = status;
        this.cancelReason = cancelReason;
    }
}
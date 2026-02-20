package com.ecommerce.backoffice.domain.product.entity;

import com.ecommerce.backoffice.domain.admin.entity.Admin;
import com.ecommerce.backoffice.domain.product.enums.ProductCategory;
import com.ecommerce.backoffice.domain.product.enums.ProductStatus;
import com.ecommerce.backoffice.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "products")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Product extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 상품 기본 정보
    @Column(nullable = false, length = 120)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private ProductCategory category;

    // 가격(원 단위) - 필요하면 BigDecimal로 바꿔도 됨
    @Column(nullable = false)
    private Long price;

    // 재고
    @Column(nullable = false)
    private Integer stock;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private ProductStatus status;

    // 등록 관리자(단방향)
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "created_admin_id", nullable = false)
    private Admin createdBy;

    public Product(String name, ProductCategory category, Long price, Integer stock, ProductStatus status, Admin createdBy) {
        this.name = name;
        this.category = category;
        this.price = price;
        this.stock = stock;
        this.status = status;
        this.createdBy = createdBy;
    }

    public void updateProduct(String name, ProductCategory category, Long price){
        this.name = name;
        this.category = category;
        this.price = price;
    }
}
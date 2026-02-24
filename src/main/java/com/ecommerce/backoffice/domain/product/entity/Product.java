package com.ecommerce.backoffice.domain.product.entity;

import com.ecommerce.backoffice.domain.admin.entity.Admin;
import com.ecommerce.backoffice.domain.product.enums.ProductCategory;
import com.ecommerce.backoffice.domain.product.enums.ProductStatus;
import com.ecommerce.backoffice.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
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
    private int stock;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private ProductStatus status;

    //재고 변경 및 상태 자동 갱신
    public void updateStock(int newStock) {
        this.stock = newStock;
        updateStatusByStock();
    }

    // 등록 관리자명(단방향)
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "created_admin_id", nullable = false)
    private Admin createdBy;

    @Builder
    public Product(String name, ProductCategory category, Long price, int stock, ProductStatus status, Admin createdBy) {
        this.name = name;
        this.category = category;
        this.price = price;
        this.updateStock(stock);
        this.status = status;
        this.createdBy = createdBy;
    }

    //상품 수정
    public void updateProduct(String name, ProductCategory category, Long price){
        this.name = name;
        this.category = category;
        this.price = price;
    }

    //상품 상태 수정
    public void updateStatus(ProductStatus status, int stock){
        this.status = status;
        this.stock = stock;
    }

    public void increaseStock(int quantity) {
        this.stock += quantity;
    }

    public void updateStatusByStock() {

        if (this.status == ProductStatus.DISCONTINUED) {
            return;
        }

        if (this.stock <= 0) {
            this.status = ProductStatus.OUT_OF_STOCK;
        } else {
            this.status = ProductStatus.ON_SALE;
        }
    }
}
package com.ecommerce.backoffice.domain.product.enums;

public enum ProductCategory {
    ELECTRONICS("전자기기"),
    FASHION("의류"),
    FOOD("식품");

    private final String categoryValue;
    ProductCategory(String categoryValue) {
        this.categoryValue = categoryValue;
    }
    public String getCategoryValue() {
        return categoryValue;
    }
}
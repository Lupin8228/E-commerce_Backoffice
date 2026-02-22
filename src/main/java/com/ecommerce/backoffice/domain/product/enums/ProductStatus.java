package com.ecommerce.backoffice.domain.product.enums;

public enum ProductStatus {
    ON_SALE("판매중"),
    OUT_OF_STOCK("품절"),
    DISCONTINUED("단종");

    private final String statusValue;
    ProductStatus(String statusValue) {
        this.statusValue = statusValue;
    }
    public String getStatusValue() {
        return statusValue;
    }
}

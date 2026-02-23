package com.ecommerce.backoffice.domain.product.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum ProductStatus {
    ON_SALE("판매중"),
    OUT_OF_STOCK("품절"),
    DISCONTINUED("단종");

    private final String statusValue;
    ProductStatus(String statusValue) {
        this.statusValue = statusValue;
    }

    @JsonValue //객체 -> JSON시 "판매중"으로 출력되게 함
    public String getStatusValue() {
        return statusValue;
    }

    @JsonCreator
    public static ProductStatus fromValue(String value) {
        for (ProductStatus status : ProductStatus.values()) {
            if (status.statusValue.equals(value)) {
                return status;
            }
        }
        return null;
    }
}

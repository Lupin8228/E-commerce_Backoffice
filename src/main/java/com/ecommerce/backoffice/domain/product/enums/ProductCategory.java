package com.ecommerce.backoffice.domain.product.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum ProductCategory {
    ELECTRONICS("전자기기"),
    FASHION("의류"),
    FOOD("식품");

    private final String categoryValue;
    ProductCategory(String categoryValue) {
        this.categoryValue = categoryValue;
    }

    @JsonValue //객체 -> JSON. db에서 꺼내서 JSON으로 응답을 줄 때 문자열로 보여주게 함
    public String getCategoryValue() {
        return categoryValue;
    }

    @JsonCreator //JSON에 문자열이 들어오면 객체로 변환해줌
    public static ProductCategory fromValue(String value){
        for (ProductCategory category : ProductCategory.values()) {
            if (category.categoryValue.equals(value)) {
                return category;
            }
        }
        return null;
    }

}
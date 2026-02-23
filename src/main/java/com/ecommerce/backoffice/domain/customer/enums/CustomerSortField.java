package com.ecommerce.backoffice.domain.customer.enums;


public enum CustomerSortField {
    NAME("name"),
    EMAIL("email"),
    PHONE("phone"),
    STATUS("status"),
    CREATED_AT("createdAt");

    private final String field;

    CustomerSortField(String field) {
        this.field = field;
    }
    public String getField() {
        return field;
    }
    public static String from(String value) {
        try {
            return CustomerSortField.valueOf(value.toUpperCase()).getField();
        } catch (IllegalArgumentException e) {
            return CREATED_AT.getField();
        }
    }
}

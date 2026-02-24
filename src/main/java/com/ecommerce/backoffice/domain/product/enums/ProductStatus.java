package com.ecommerce.backoffice.domain.product.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ProductStatus {
    ON_SALE,
    OUT_OF_STOCK,
    DISCONTINUED
}

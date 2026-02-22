package com.ecommerce.backoffice.domain.order.dto.request;

import lombok.Builder;

@Builder
public record OrderSearchRequest(
        String keyword,
        String sortBy,
        String status,
        String direction
) {
}

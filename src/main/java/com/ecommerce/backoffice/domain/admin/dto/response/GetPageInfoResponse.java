package com.ecommerce.backoffice.domain.admin.dto.response;

import org.springframework.data.domain.Page;

public record GetPageInfoResponse(
        int page,
        int size,
        long totalElements,
        int totalPages
) {
    public static GetPageInfoResponse from(Page<?> page) {
        return new GetPageInfoResponse(
                page.getNumber() + 1,
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages()
        );
    }
}

package com.ecommerce.backoffice.domain.product.dto.response;

import org.springframework.data.domain.Page;

import java.util.List;

public record GetProductPageResponse(
        List<GetAllProductResponse> products,
        long totalElements,
        int totalPage,
        int currentPage,
        int size
){
    //Page 객체를 dto로 바꿔줌
    public static GetProductPageResponse from(Page<GetAllProductResponse> page) {
        return new GetProductPageResponse(
                page.getContent(),
                page.getTotalElements(),
                page.getTotalPages(),
                page.getNumber()+1, //1부터 시작
                page.getSize()
        );
    }
}


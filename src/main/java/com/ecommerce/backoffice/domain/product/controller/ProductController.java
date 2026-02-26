package com.ecommerce.backoffice.domain.product.controller;

import com.ecommerce.backoffice.domain.product.dto.request.CreateProductRequest;
import com.ecommerce.backoffice.domain.product.dto.request.UpdateProductRequest;
import com.ecommerce.backoffice.domain.product.dto.request.UpdateProductStatusRequest;
import com.ecommerce.backoffice.domain.product.dto.response.*;
import com.ecommerce.backoffice.domain.product.enums.ProductCategory;
import com.ecommerce.backoffice.domain.product.enums.ProductStatus;
import com.ecommerce.backoffice.domain.product.service.ProductService;
import com.ecommerce.backoffice.global.common.ApiResponse;
import com.ecommerce.backoffice.global.security.UserDetailsImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;

    //상품 생성
    @PostMapping
    public ResponseEntity<ApiResponse<CreateProductResponse>> saveProduct(
            @AuthenticationPrincipal UserDetailsImpl principal,
            @Valid @RequestBody CreateProductRequest request
    ){
        Long adminId = principal.getAdmin().getId();
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(productService.save(adminId, request)));
    }

    //상품 리스트 조회
    @GetMapping
    public ResponseEntity<ApiResponse<GetProductPageResponse>> getAllProducts(
            @RequestParam(required = false) String productName,
            @RequestParam(required = false) ProductCategory category,
            @RequestParam(required = false) ProductStatus status,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sort
            ) {
        GetProductPageResponse response = productService.getProduct(productName, category, status, page, size, sortBy, sort);
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success(response));
    }

    //상품 상세 조회
    @GetMapping("/{productId}")
    public ResponseEntity<ApiResponse<GetDetailProductResponse>> getDetailProduct(
            @PathVariable Long productId
    ){
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success(productService.getProduct(productId)));
    }

    //상품 업데이트
    @PatchMapping("/{productId}")
    public ResponseEntity<ApiResponse<UpdateProductResponse>> updateProduct(
            @PathVariable Long productId,
            @Valid @RequestBody UpdateProductRequest request
    ){
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success(productService.updateProduct(productId, request)));
    }

    //상품 상태 변경
    @PatchMapping("/{productId}/status")
    public ResponseEntity<ApiResponse<UpdateProductStatusResponse>> updateStatus(
            @PathVariable Long productId,
            @Valid @RequestBody UpdateProductStatusRequest request
    ) {
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success(productService.updateStatus(productId, request)));
    }

    //상품 삭제
    @DeleteMapping("/{productId}")
    public ResponseEntity<ApiResponse<Void>> deleteProduct(
            @PathVariable Long productId
    ) {
        productService.deleteProduct(productId);
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success(null));

    }

    //상품별 리뷰 조회
    @GetMapping("/{productId}/reviews")
    public ResponseEntity<ApiResponse<ProductReviewResponse>> getProductReviews(
            @PathVariable Long productId
    ){
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success(productService.getProductReview(productId)));
    }
}

package com.ecommerce.backoffice.domain.product.controller;

import com.ecommerce.backoffice.domain.product.dto.request.CreateProductRequest;
import com.ecommerce.backoffice.domain.product.dto.request.UpdateProductRequest;
import com.ecommerce.backoffice.domain.product.dto.response.*;
import com.ecommerce.backoffice.domain.product.enums.ProductCategory;
import com.ecommerce.backoffice.domain.product.enums.ProductStatus;
import com.ecommerce.backoffice.domain.product.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    //상품 생성
    @PostMapping("/products")
    public ResponseEntity<CreateProductResponse> saveProduct(
            @RequestAttribute Long adminId,
            @Valid @RequestBody CreateProductRequest request
    ){
        return ResponseEntity.status(HttpStatus.CREATED).body(productService.save(adminId, request));
    }

    //상품 리스트 조회
    @GetMapping("/products")
    public ResponseEntity<GetProductPageResponse> getAllProducts(
            @RequestParam(required = false) String productName,
            @RequestParam(required = false) ProductCategory category,
            @RequestParam(required = false)ProductStatus status,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sort
            ) {
        GetProductPageResponse response = productService.getProduct(productName, category, status, page, size, sortBy, sort);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    //상품 상세 조회
    @GetMapping("/products/{id}")
    public ResponseEntity<GetDetailProductResponse> getDetailProduct(
            @PathVariable Long productId
    ){
        return ResponseEntity.status(HttpStatus.OK).body(productService.getProduct(productId));
    }

    //상품 업데이트
    @PatchMapping("/products/{id}")
    public ResponseEntity<UpdateProductResponse> updateProduct(
            @PathVariable Long productId,
            @Valid @RequestBody UpdateProductRequest request
    ){
        return ResponseEntity.status(HttpStatus.OK).body(productService.updateProduct(productId, request));
    }

    //상품 삭제
    @DeleteMapping("/products/{id}")
    public ResponseEntity<Void> deleteProduct(
            @PathVariable Long productId
    ) {
        productService.deleteProduct(productId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}

package com.ecommerce.backoffice.domain.product.service;

import com.ecommerce.backoffice.domain.admin.entity.Admin;
import com.ecommerce.backoffice.domain.admin.repository.AdminRepository;
import com.ecommerce.backoffice.domain.product.dto.request.CreateProductRequest;
import com.ecommerce.backoffice.domain.product.dto.request.UpdateProductRequest;
import com.ecommerce.backoffice.domain.product.dto.response.*;
import com.ecommerce.backoffice.domain.product.entity.Product;
import com.ecommerce.backoffice.domain.product.enums.ProductCategory;
import com.ecommerce.backoffice.domain.product.enums.ProductStatus;
import com.ecommerce.backoffice.domain.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final AdminRepository adminRepository;

    //상품 생성
    @Transactional
    public CreateProductResponse save(Long adminId, CreateProductRequest request) {
        //현재 상품을 등록하려는 관리자 조회
        Admin admin = adminRepository.findById(adminId).orElseThrow(
                () -> new IllegalStateException("관리자가 없습니다.")
        );

        Product savedProduct = productRepository.save(
                Product.builder()
                        .name(request.name())
                        .category(ProductCategory.ELECTRONICS)
                        .price(request.price())
                        .stock(request.stock())
                        .status(ProductStatus.ON_SALE)
                        .build()
        );
        return CreateProductResponse.from(savedProduct);
    }

    //상품 리스트 조회
    @Transactional(readOnly = true)
    public GetProductPageResponse getProduct(
            String productName, ProductCategory category, ProductStatus status,
            int page, int size, String sortBy, String sort
    ){
        //정렬
        Sort.Direction direction = "desc".equalsIgnoreCase(sort) ? Sort.Direction.DESC : Sort.Direction.ASC;
        Sort sortObj = Sort.by(direction, sortBy);

        //Pageable 생성
        Pageable pageable = PageRequest.of(page-1, size, sortObj);

        Page<GetAllProductResponse> productPage = (Page<GetAllProductResponse>) productRepository.findAllProducts(productName, category, status, pageable);

        return new GetProductPageResponse(
                productPage.getContent(),
                productPage.getTotalElements(),
                productPage.getTotalPages(),
                productPage.getNumber()+1,
                productPage.getSize()
        );
    }

    //상품 상세 조회
    @Transactional
    public GetDetailProductResponse getProduct(Long productId){
        Product product = productRepository.findById(productId).orElseThrow(
                () -> new IllegalStateException("없는 상품입니다.")
        );

        //댓글 리스트 조회

        return new GetDetailProductResponse(
                product.getId(),
                product.getName(),
                product.getCategory(),
                product.getPrice(),
                product.getStock(),
                product.getStatus(),
                product.getCreatedAt()
        );
    }

    //상품 수정
    @Transactional
    public UpdateProductResponse updateProduct(Long productId, UpdateProductRequest request) {
        Product product = productRepository.findById(productId).orElseThrow(
                () -> new IllegalStateException("없는 상품입니다.")
        );

        product.updateProduct(
                request.name(),
                request.category(),
                request.price()
        );

        return UpdateProductResponse.from(product);
    }

    //상품 삭제
    @Transactional
    public void deleteProduct(Long productId){
        Product product = productRepository.findById(productId).orElseThrow(
                () -> new IllegalStateException("상품이 존재하지 않습니다.")
        );

        //삭제할때 댓글도 같이 삭제하게 만들기

        productRepository.deleteById(productId);
    }
}

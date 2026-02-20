package com.ecommerce.backoffice.domain.product.service;

import com.ecommerce.backoffice.domain.admin.entity.Admin;
import com.ecommerce.backoffice.domain.admin.repository.AdminRepository;
import com.ecommerce.backoffice.domain.product.dto.request.CreateProductRequest;
import com.ecommerce.backoffice.domain.product.dto.request.UpdateProductRequest;
import com.ecommerce.backoffice.domain.product.dto.response.CreateProductResponse;
import com.ecommerce.backoffice.domain.product.dto.response.GetAllProductResponse;
import com.ecommerce.backoffice.domain.product.dto.response.GetDetailProductResponse;
import com.ecommerce.backoffice.domain.product.dto.response.UpdateProductResponse;
import com.ecommerce.backoffice.domain.product.entity.Product;
import com.ecommerce.backoffice.domain.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
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
    public CreateProductResponse saveProduct(Long adminId, CreateProductRequest request) {
        //현재 상품을 등록하려는 관리자 조회
        Admin admin = adminRepository.findById(adminId).orElseThrow(
                () -> new IllegalArgumentException("존재하지 않는 관리자입니다.")
        );

        //dto를 엔티티로 변환
        Product product = request.toProduct(admin);

        //db에 저장
        Product savedProduct = productRepository.save(product);

        //저장된 결과를 응답 dto로 변환해서 반환
        return new CreateProductResponse(
                savedProduct.getId(),
                savedProduct.getName(),
                savedProduct.getCategory(),
                savedProduct.getPrice(),
                savedProduct.getStock(),
                savedProduct.getStatus(),
                savedProduct.getCreatedAt()
        );
    }

    //상품 리스트 조회
    @Transactional(readOnly = true)
    public List<GetAllProductResponse> getProduct(){
        List<Product> products = productRepository.findAll();

        return products
                .stream()
                .map(GetAllProductResponse::from)
                .toList();
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
                () -> new IllegalStateException("없는 상품입니다.")
        );

        //삭제할때 댓글도 같이 삭제하게 만들기

        productRepository.deleteById(productId);
    }
}

package com.ecommerce.backoffice.domain.review.service;

import com.ecommerce.backoffice.domain.customer.entity.Customer;
import com.ecommerce.backoffice.domain.customer.repository.CustomerRepository;
import com.ecommerce.backoffice.domain.product.entity.Product;
import com.ecommerce.backoffice.domain.product.repository.ProductRepository;
import com.ecommerce.backoffice.domain.review.dto.request.CreateReviewRequest;
import com.ecommerce.backoffice.domain.review.dto.response.CreateReviewResponse;
import com.ecommerce.backoffice.domain.review.dto.response.GetDetailReviewResponse;
import com.ecommerce.backoffice.domain.review.dto.response.GetReviewPageResponse;
import com.ecommerce.backoffice.domain.review.dto.response.GetReviewResponse;
import com.ecommerce.backoffice.domain.review.entity.Review;
import com.ecommerce.backoffice.domain.review.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final ProductRepository productRepository;
    private final CustomerRepository customerRepository;

    //리뷰 등록
    @Transactional
    public CreateReviewResponse createReview(Long productId, CreateReviewRequest request) {
        productRepository.findById(productId).orElseThrow(
                () -> new IllegalStateException("존재하지 않는 상품입니다.")
        );

        Review savedReview = reviewRepository.save(
                Review.builder()
                        .rating(request.rating())
                        .description(request.description())
                        .build()
        );
        return CreateReviewResponse.from(savedReview);
    }

    //리뷰 리스트 조회
    @Transactional
    public GetReviewPageResponse getAllReview(
            String search, int rating, int page, int size, String sortBy, String sort
    ) {
        //정렬 (기본값: 작성일 내림차순)
        Sort.Direction direction = "asc".equalsIgnoreCase(sort) ? Sort.Direction.ASC : Sort.Direction.DESC;
        Sort sortObj = Sort.by(direction, (sortBy != null) ? sortBy : "createdAt");

        //페이지 생성
        Pageable pageable = PageRequest.of(page-1, size, sortObj);

        //필터링된 데이터 조회
        Page<GetReviewResponse> reviewPage = reviewRepository.findAllReviews(search, rating, pageable);

        //최종 응답
        return new GetReviewPageResponse(
                reviewPage.getContent(),
                reviewPage.getTotalElements(),
                reviewPage.getTotalPages(),
                reviewPage.getNumber()+1,
                reviewPage.getSize()
        );
    }

    //리뷰 상세 조회
    @Transactional
    public GetDetailReviewResponse getReview(Long customerId, Long reviewId, Long productId) {
        Review review = reviewRepository.findById(reviewId).orElseThrow(
                () -> new IllegalStateException("존재하지 않는 리뷰입니다.")
        );
        Customer customer = customerRepository.findById(customerId).orElseThrow(
                () -> new IllegalStateException("존재하지 않는 고객입니다.")
        );
        Product product = productRepository.findById(productId).orElseThrow(
                () -> new IllegalStateException("존재하지 않는 상품입니다.")
        );

        return new GetDetailReviewResponse(
                product.getName(),
                customer.getName(),
                customer.getEmail(),
                review.getCreatedAt(),
                review.getRating(),
                review.getDescription()
        );
    }

    //리뷰 삭제
    @Transactional
    public void deleteReview(Long reviewId, Long customerId) {
        Review review = reviewRepository.findById(reviewId).orElseThrow(
                () -> new IllegalStateException("댓글이 존재하지 않습니다.")
        );
        Customer customer = customerRepository.findById(customerId).orElseThrow(
                () -> new IllegalStateException("존재하지 않는 고객입니다.")
        );

        productRepository.deleteById(reviewId);
    }
}

package com.ecommerce.backoffice.domain.review.service;

import com.ecommerce.backoffice.domain.customer.entity.Customer;
import com.ecommerce.backoffice.domain.customer.repository.CustomerRepository;
import com.ecommerce.backoffice.domain.product.entity.Product;
import com.ecommerce.backoffice.domain.product.repository.ProductRepository;
import com.ecommerce.backoffice.domain.review.dto.request.CreateReviewRequest;
import com.ecommerce.backoffice.domain.review.dto.response.CreateReviewResponse;
import com.ecommerce.backoffice.domain.review.dto.response.GetDetailReviewResponse;
import com.ecommerce.backoffice.domain.review.dto.response.GetReviewResponse;
import com.ecommerce.backoffice.domain.review.entity.Review;
import com.ecommerce.backoffice.domain.review.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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
    public List<GetReviewResponse> getAllReview(Long productId, Long customerId) {
        Customer customer = customerRepository.findById(customerId).orElseThrow(
                () -> new IllegalStateException("존재하지 않는 고객입니다.")
        );
        Product product = productRepository.findById(productId).orElseThrow(
                () -> new IllegalStateException("존재하지 않는 상품입니다.")
        );

        List<Review> reviews = reviewRepository.findByCustomerId(customerId);
        return reviews
                .stream()
                .map((Review review) -> GetReviewResponse.from(review, product, customer))
                .toList();
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

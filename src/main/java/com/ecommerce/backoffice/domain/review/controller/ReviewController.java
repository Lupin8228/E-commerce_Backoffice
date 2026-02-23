package com.ecommerce.backoffice.domain.review.controller;

import com.ecommerce.backoffice.domain.review.dto.request.CreateReviewRequest;
import com.ecommerce.backoffice.domain.review.dto.response.CreateReviewResponse;
import com.ecommerce.backoffice.domain.review.dto.response.GetDetailReviewResponse;
import com.ecommerce.backoffice.domain.review.dto.response.GetReviewPageResponse;
import com.ecommerce.backoffice.domain.review.service.ReviewService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    //리뷰 생성
    @PostMapping("/reviews")
    public ResponseEntity<CreateReviewResponse> createReview(
            @RequestAttribute Long productId,
            @Valid  @RequestBody CreateReviewRequest request
    ) {
        return ResponseEntity.status(HttpStatus.CREATED).body(reviewService.createReview(productId, request));
    }


    //리뷰 목록 조회
    @GetMapping("/reviews")
    public ResponseEntity<GetReviewPageResponse> getAllReviews(
            @RequestAttribute Long productId,
            @RequestAttribute Long customerId,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) int rating,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sort
    ){
        GetReviewPageResponse response = reviewService.getAllReview(search, rating, page, size, sortBy, sort);
        return ResponseEntity.ok(response);
    }

    //리뷰 상세 조회
    @GetMapping("/reviews/{id}")
    public ResponseEntity<GetDetailReviewResponse> getDetailReview(
            @PathVariable Long id, //reviewId
            @RequestAttribute Long productId,
            @RequestAttribute Long customerId
    ) {
        return ResponseEntity.status(HttpStatus.OK).body(reviewService.getReview(customerId, id, productId));
    }

    //리뷰 삭제
    @DeleteMapping("/reviews/{id}")
    public ResponseEntity<Void> deleteReview(
            @PathVariable Long id, //reviewId
            @RequestAttribute Long customerId
    ) {
        reviewService.deleteReview(id, customerId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}

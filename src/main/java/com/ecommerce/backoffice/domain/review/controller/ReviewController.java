package com.ecommerce.backoffice.domain.review.controller;

import com.ecommerce.backoffice.domain.review.dto.response.GetDetailReviewResponse;
import com.ecommerce.backoffice.domain.review.dto.response.GetReviewPageResponse;
import com.ecommerce.backoffice.domain.review.service.ReviewService;
import com.ecommerce.backoffice.global.common.ApiResponse;
import com.ecommerce.backoffice.global.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class ReviewController {

    private final ReviewService reviewService;

    //리뷰 생성
//    @PostMapping
//    public ResponseEntity<CreateReviewResponse> createReview(
//            @RequestAttribute Long productId,
//            @Valid  @RequestBody CreateReviewRequest request
//    ) {
//        return ResponseEntity.status(HttpStatus.CREATED).body(reviewService.createReview(productId, request));
//    }

    //리뷰 목록 조회
    @GetMapping("/reviews")
    public ResponseEntity<ApiResponse<GetReviewPageResponse>> getAllReviews(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) Integer rating,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sort
    ){
        GetReviewPageResponse response = reviewService.getAllReview(search, rating, page, size, sortBy, sort);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    //리뷰 상세 조회
    @GetMapping("/reviews/{id}")
    public ResponseEntity<ApiResponse<GetDetailReviewResponse>> getDetailReview(
            @AuthenticationPrincipal UserDetailsImpl principal,
            @PathVariable Long id, //reviewId
            @RequestParam(name = "productId", required = true) Long productId, // 필수값 설정
            @RequestParam(name = "customerId", required = true) Long customerId
    ) {
        return ResponseEntity.ok(ApiResponse.success(reviewService.getReview(id, productId, customerId)));
    }

    //리뷰 삭제
    @DeleteMapping("/reviews/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteReview(
            @AuthenticationPrincipal UserDetailsImpl principal,
            @PathVariable Long id //reviewId
    ) {
        Long adminId = principal.getAdmin().getId();

        reviewService.deleteReview(id, adminId);
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success(null));
    }

}

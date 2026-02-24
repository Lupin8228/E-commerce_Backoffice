package com.ecommerce.backoffice.domain.review.controller;

import com.ecommerce.backoffice.domain.review.dto.response.GetDetailReviewResponse;
import com.ecommerce.backoffice.domain.review.dto.response.GetReviewPageResponse;
import com.ecommerce.backoffice.domain.review.service.ReviewService;
import com.ecommerce.backoffice.global.security.UserDetailsImpl;
import jakarta.validation.Valid;
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
    public ResponseEntity<GetReviewPageResponse> getAllReviews(
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
    @GetMapping("/{productId}/reviews/{id}")
    public ResponseEntity<GetDetailReviewResponse> getDetailReview(
            @AuthenticationPrincipal UserDetailsImpl principal,
            @PathVariable Long productId,
            @PathVariable Long id //reviewId
    ) {
        Long customerId = principal.getAdmin().getId();
        return ResponseEntity.status(HttpStatus.OK).body(reviewService.getReview(customerId, id, productId));
    }

    //리뷰 삭제
    @DeleteMapping("/reviews/{id}")
    public ResponseEntity<Void> deleteReview(
            @AuthenticationPrincipal UserDetailsImpl principal,
            @PathVariable Long id //reviewId
    ) {
        Long adminId = principal.getAdmin().getId();

        reviewService.deleteReview(id, adminId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}

package com.ecommerce.backoffice.domain.review.repository;

import com.ecommerce.backoffice.domain.review.dto.response.GetReviewResponse;
import com.ecommerce.backoffice.domain.review.entity.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    List<Review> findByCustomerId(Long customerId);

    @Query("""
        SELECT new com.ecommerce.backoffice.domain.review.dto.response.GetReviewResponse(
            r.id, p.id, c.name, p.name, r.rating, r.description, r.createdAt
        )
        FROM Review r
        JOIN r.order o
        JOIN r.customer c
        JOIN r.product p
        WHERE (:rating IS NULL OR r.rating = :rating)
        AND (:search IS NULL OR c.name LIKE %:search% OR p.name LIKE %:search%)
    """)
    Page<GetReviewResponse> findAllReviews(
            @Param("search") String search,
            @Param("rating") int rating,
            Pageable pageable
    );
}

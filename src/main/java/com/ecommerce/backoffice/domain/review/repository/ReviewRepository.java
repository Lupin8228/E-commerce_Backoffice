package com.ecommerce.backoffice.domain.review.repository;

import com.ecommerce.backoffice.domain.review.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<Review, Integer> {
}

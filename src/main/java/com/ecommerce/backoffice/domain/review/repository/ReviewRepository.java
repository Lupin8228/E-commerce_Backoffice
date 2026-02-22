package com.ecommerce.backoffice.domain.review.repository;

import com.ecommerce.backoffice.domain.review.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    List<Review> findByCustomerId(Long customerId);
}

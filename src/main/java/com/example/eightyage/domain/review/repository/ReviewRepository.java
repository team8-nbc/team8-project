package com.example.eightyage.domain.review.repository;

import com.example.eightyage.domain.product.entity.Product;
import com.example.eightyage.domain.review.entity.Review;
import com.example.eightyage.global.exception.NotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {

    @Query("SELECT r FROM Review r WHERE r.id = :reviewId AND r.deletedAt IS NULL")
    Optional<Review> findById(@Param("reviewId") Long reviewId);

    Page<Review> findByProductIdAndProductDeletedAtIsNull(Long productId, Pageable pageable);

    @Query("SELECT r FROM Review r JOIN FETCH r.user JOIN FETCH r.product WHERE r.product.id = :productId AND r.deletedAt IS NULL")
    List<Review> findReviewsByProductId(@Param("productId") Long productId);
}

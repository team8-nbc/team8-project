package com.example.eightyage.domain.review.repository;

import com.example.eightyage.domain.review.entity.Review;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Random;

@Repository
@RequiredArgsConstructor
public class ReviewBulkRepository {

    private final JdbcTemplate jdbcTemplate;
    private final int BATCH_SIZE = 1000;

    public void bulkInsertReviews(List<Review> reviews) {
        String sql = "INSERT INTO review (user_id, product_id, score) values (?, ?, ?)";

        Random random = new Random();

        jdbcTemplate.batchUpdate(sql, reviews, BATCH_SIZE, (ps, argument) -> {
            ps.setInt(1, 1);
            ps.setInt(2, random.nextInt(1000) + 1);
            ps.setDouble(3, random.nextDouble() * 5);
        });
    }

}

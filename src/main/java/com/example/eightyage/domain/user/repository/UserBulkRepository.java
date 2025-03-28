package com.example.eightyage.domain.user.repository;

import com.example.eightyage.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;

@Repository
@RequiredArgsConstructor
public class UserBulkRepository {

    private final JdbcTemplate jdbcTemplate;
    private final int BATCH_SIZE = 1000;

    public void bulkInsertUsers(List<User> users) {
        String sql = "INSERT INTO user (email, password, nickname, deleted_at) values (?, ?, ?, ?)";

        Random random = new Random();

        jdbcTemplate.batchUpdate(sql, users, BATCH_SIZE, (ps, argument) -> {
            ps.setString(1, argument.getEmail());
            ps.setString(2, argument.getPassword());
            ps.setString(3, argument.getNickname());
            ps.setString(4, random.nextBoolean() ? null : LocalDateTime.now().toString());  // 랜덤으로 유저 삭제
        });
    }
}

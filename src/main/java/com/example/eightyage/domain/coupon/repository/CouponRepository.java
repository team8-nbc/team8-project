package com.example.eightyage.domain.coupon.repository;

import com.example.eightyage.domain.coupon.entity.Coupon;
import com.example.eightyage.domain.coupon.entity.CouponState;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CouponRepository extends JpaRepository<Coupon, Long> {
    boolean existsByUserIdAndEventId(Long userId, Long eventId);
    Page<Coupon> findAllByUserIdAndState(Long userId, CouponState state, Pageable pageable);
}

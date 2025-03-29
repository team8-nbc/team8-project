package com.example.eightyage.domain.coupon.repository;

import com.example.eightyage.domain.coupon.entity.IssuedCoupon;
import com.example.eightyage.domain.coupon.status.Status;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IssuedCouponRepository extends JpaRepository<IssuedCoupon, Long> {
    boolean existsByUserIdAndCouponId(Long userId, Long couponId);
    Page<IssuedCoupon> findAllByUserIdAndStatus(Long userId, Status status, Pageable pageable);
}

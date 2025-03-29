package com.example.eightyage.domain.coupon.controller;

import com.example.eightyage.domain.coupon.dto.response.IssuedCouponResponseDto;
import com.example.eightyage.domain.coupon.service.IssuedCouponService;
import com.example.eightyage.global.dto.AuthUser;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class IssuedCouponController {

    private final IssuedCouponService issuedCouponService;

    @PostMapping("/v1/coupons/{couponId}/issues")
    public ResponseEntity<IssuedCouponResponseDto> issueCoupon(@AuthenticationPrincipal AuthUser authUser, @PathVariable Long couponId) {
        return ResponseEntity.ok(issuedCouponService.issueCoupon(authUser, couponId));
    }

    @GetMapping("/v1/coupons/my")
    public ResponseEntity<Page<IssuedCouponResponseDto>> getMyCoupons(
            @AuthenticationPrincipal AuthUser authUser,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(issuedCouponService.getMyCoupons(authUser, page, size));
    }

    @GetMapping("/v1/coupons/{issuedCouponId}")
    public ResponseEntity<IssuedCouponResponseDto> getCoupon(@AuthenticationPrincipal AuthUser authUser, @PathVariable Long issuedCouponId) {
        return ResponseEntity.ok(issuedCouponService.getCoupon(authUser, issuedCouponId));
    }
}

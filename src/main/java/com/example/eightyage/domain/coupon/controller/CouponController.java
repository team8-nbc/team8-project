package com.example.eightyage.domain.coupon.controller;

import com.example.eightyage.domain.coupon.dto.request.CouponRequestDto;
import com.example.eightyage.domain.coupon.dto.response.CouponResponseDto;
import com.example.eightyage.domain.coupon.service.CouponService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class CouponController {

    private final CouponService couponService;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/v1/coupons")
    public ResponseEntity<CouponResponseDto> createCoupon(@Valid @RequestBody CouponRequestDto couponRequestDto) {
        return ResponseEntity.ok(couponService.saveCoupon(couponRequestDto));
    }

    @GetMapping("/v1/coupons")
    public ResponseEntity<Page<CouponResponseDto>> getCoupons(@RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(couponService.getCoupons(page, size));
    }

    @GetMapping("/v1/coupons/{couponId}")
    public ResponseEntity<CouponResponseDto> getCoupon(@PathVariable long couponId) {
        return ResponseEntity.ok(couponService.getCoupon(couponId));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/v1/coupons/{couponId}")
    public ResponseEntity<CouponResponseDto> updateCoupon(@PathVariable long couponId, @Valid @RequestBody CouponRequestDto couponRequestDto) {
        return ResponseEntity.ok(couponService.updateCoupon(couponId, couponRequestDto));
    }
}

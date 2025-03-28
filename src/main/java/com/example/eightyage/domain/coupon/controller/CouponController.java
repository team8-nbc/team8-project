//package com.example.eightyage.domain.coupon.controller;
//
//import com.example.eightyage.domain.coupon.dto.response.CouponResponseDto;
//import com.example.eightyage.domain.coupon.service.CouponService;
//import com.example.eightyage.global.dto.AuthUser;
//import lombok.RequiredArgsConstructor;
//import org.springframework.data.domain.Page;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.core.annotation.AuthenticationPrincipal;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//
//@RestController
//@RequestMapping("/api")
//@RequiredArgsConstructor
//public class CouponController {
//
//    private final CouponService couponService;
//
//    @PostMapping("/v1/events/{eventId}/coupons")
//    public ResponseEntity<CouponResponseDto> issueCoupon(@AuthenticationPrincipal AuthUser authUser, @PathVariable Long eventId) {
//        return ResponseEntity.ok(couponService.issueCoupon(authUser, eventId));
//    }
//
//    @GetMapping("/v1/coupons/my")
//    public ResponseEntity<Page<CouponResponseDto>> getMyCoupons(
//            @AuthenticationPrincipal AuthUser authUser,
//            @RequestParam(defaultValue = "1") int page,
//            @RequestParam(defaultValue = "10") int size) {
//        return ResponseEntity.ok(couponService.getMyCoupons(authUser, page, size));
//    }
//
//    @GetMapping("/v1/coupons/{couponId}")
//    public ResponseEntity<CouponResponseDto> getCoupon(@AuthenticationPrincipal AuthUser authUser,@PathVariable Long couponId) {
//        return ResponseEntity.ok(couponService.getCoupon(authUser, couponId));
//    }
//}

package com.example.eightyage.domain.coupon.dto.response;

import com.example.eightyage.domain.coupon.entity.CouponState;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class CouponResponseDto {

    private final String couponCode;
    private final CouponState state;
    private final String username;
    private final String eventname;

    private final LocalDateTime startAt;
    private final LocalDateTime endAt;

    public CouponResponseDto(String couponCode, CouponState state,
                             String username, String eventname,
                             LocalDateTime startAt, LocalDateTime endAt) {
        this.couponCode = couponCode;
        this.state = state;
        this.username = username;
        this.eventname = eventname;
        this.startAt = startAt;
        this.endAt = endAt;
    }
}

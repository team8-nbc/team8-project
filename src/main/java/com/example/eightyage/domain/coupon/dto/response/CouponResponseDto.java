package com.example.eightyage.domain.coupon.dto.response;

import com.example.eightyage.domain.coupon.couponstate.CouponState;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class CouponResponseDto {

    private final String name;
    private final String description;
    private final int quantity;
    private final LocalDateTime startDate;
    private final LocalDateTime endDate;
    private final CouponState state;


    public CouponResponseDto(String name, String description, int quantity, LocalDateTime startDate, LocalDateTime endDate, CouponState state) {
        this.name = name;
        this.description = description;
        this.quantity = quantity;
        this.startDate = startDate;
        this.endDate = endDate;
        this.state = state;
    }
}

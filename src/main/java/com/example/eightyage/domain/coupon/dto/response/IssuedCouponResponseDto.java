package com.example.eightyage.domain.coupon.dto.response;

import com.example.eightyage.domain.coupon.status.Status;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class IssuedCouponResponseDto {

    private final String serialCode;
    private final Status status;
    private final String username;
    private final String eventname;

    private final LocalDateTime startAt;
    private final LocalDateTime endAt;

    public IssuedCouponResponseDto(String serialCode, Status status,
                                   String username, String eventname,
                                   LocalDateTime startAt, LocalDateTime endAt) {
        this.serialCode = serialCode;
        this.status = status;
        this.username = username;
        this.eventname = eventname;
        this.startAt = startAt;
        this.endAt = endAt;
    }
}

package com.example.eightyage.domain.coupon.entity;

import com.example.eightyage.domain.coupon.couponstate.CouponState;
import com.example.eightyage.domain.coupon.dto.request.CouponRequestDto;
import com.example.eightyage.domain.coupon.dto.response.CouponResponseDto;
import com.example.eightyage.global.entity.TimeStamped;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
public class Coupon extends TimeStamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String description;
    private int quantity;
    @Column(name="start_at")
    private LocalDateTime startDate;
    @Column(name = "end_at")
    private LocalDateTime endDate;
    @Enumerated(EnumType.STRING)
    private CouponState state;

    public Coupon(String name, String description, int quantity, LocalDateTime startDate, LocalDateTime endDate) {
        this.name = name;
        this.description = description;
        this.quantity = quantity;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public CouponResponseDto toDto() {
        return new CouponResponseDto(
                this.getName(),
                this.getDescription(),
                this.getQuantity(),
                this.getStartDate(),
                this.getEndDate(),
                this.getState()
        );
    }

    public void update(CouponRequestDto couponRequestDto) {
        this.name = couponRequestDto.getName();
        this.description = couponRequestDto.getDescription();
        this.quantity = couponRequestDto.getQuantity();
        this.startDate = couponRequestDto.getStartDate();
        this.endDate = couponRequestDto.getEndDate();
    }

    public boolean isValidAt(LocalDateTime time) {
        return (startDate.isBefore(time) || startDate.isEqual(time)) && (endDate.isAfter(time) || endDate.isEqual(time));
    }

    public void updateStateAt(LocalDateTime time) {
        CouponState newState = isValidAt(time) ? CouponState.VALID : CouponState.INVALID;
        this.state = newState;
    }
}

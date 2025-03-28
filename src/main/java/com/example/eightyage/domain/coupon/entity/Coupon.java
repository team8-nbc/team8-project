package com.example.eightyage.domain.coupon.entity;

import com.example.eightyage.domain.coupon.dto.response.CouponResponseDto;
import com.example.eightyage.domain.event.entity.Event;
import com.example.eightyage.domain.user.entity.User;
import com.example.eightyage.global.entity.TimeStamped;
import com.example.eightyage.global.util.RandomCodeGenerator;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Coupon extends TimeStamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String couponCode;

    @Enumerated(EnumType.STRING)
    private CouponState state;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    private Event event;

    public static Coupon create(User user, Event event) {
        return Coupon.builder()
                .couponCode(RandomCodeGenerator.generateCouponCode(10))
                .state(CouponState.VALID)
                .user(user)
                .event(event)
                .build();
    }

    public CouponResponseDto toDto() {
        return new CouponResponseDto(
                this.couponCode,
                this.state,
                this.user.getNickname(),
                this.event.getName(),
                this.event.getStartDate(),
                this.event.getEndDate()
        );
    }
}

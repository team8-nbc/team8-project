package com.example.eightyage.domain.coupon.entity;

import com.example.eightyage.domain.coupon.dto.response.IssuedCouponResponseDto;
import com.example.eightyage.domain.coupon.status.Status;
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
public class IssuedCoupon extends TimeStamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String serialCode;

    @Enumerated(EnumType.STRING)
    private Status status;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    private Coupon coupon;

    public static IssuedCoupon create(User user, Coupon coupon) {
        return IssuedCoupon.builder()
                .serialCode(RandomCodeGenerator.generateCouponCode(10))
                .status(Status.VALID)
                .user(user)
                .coupon(coupon)
                .build();
    }

    public IssuedCouponResponseDto toDto() {
        return new IssuedCouponResponseDto(
                this.serialCode,
                this.status,
                this.user.getNickname(),
                this.coupon.getName(),
                this.coupon.getStartDate(),
                this.coupon.getEndDate()
        );
    }
}

package com.example.eightyage.domain.coupon.service;

import com.example.eightyage.domain.coupon.dto.request.CouponRequestDto;
import com.example.eightyage.domain.coupon.dto.response.CouponResponseDto;
import com.example.eightyage.domain.coupon.entity.Coupon;
import com.example.eightyage.domain.coupon.couponstate.CouponState;
import com.example.eightyage.domain.coupon.repository.CouponRepository;
import com.example.eightyage.global.exception.BadRequestException;
import com.example.eightyage.global.exception.ErrorMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class CouponService {

    private final CouponRepository couponRepository;
    private final StringRedisTemplate stringRedisTemplate;

    public CouponResponseDto saveCoupon(CouponRequestDto couponRequestDto) {
        Coupon coupon = new Coupon(
                couponRequestDto.getName(),
                couponRequestDto.getDescription(),
                couponRequestDto.getQuantity(),
                couponRequestDto.getStartDate(),
                couponRequestDto.getEndDate()
        );

        checkCouponState(coupon);

        Coupon savedCoupon = couponRepository.save(coupon);

        stringRedisTemplate.opsForValue().set("event:quantity:" + savedCoupon.getId(), String.valueOf(savedCoupon.getQuantity()));

        return savedCoupon.toDto();
    }

    public Page<CouponResponseDto> getCoupons(int page, int size) {
        Pageable pageable = PageRequest.of(page-1, size);
        Page<Coupon> events = couponRepository.findAll(pageable);

        // 모든 events들 checkState로 state 상태 갱신하기
        events.forEach(this::checkCouponState);

        return events.map(Coupon::toDto);
    }

    public CouponResponseDto getCoupon(long couponId) {
        Coupon coupon = findByIdOrElseThrow(couponId);

        checkCouponState(coupon);

        return coupon.toDto();
    }

    public CouponResponseDto updateCoupon(long couponId, CouponRequestDto couponRequestDto) {
        Coupon coupon = findByIdOrElseThrow(couponId);

        coupon.update(couponRequestDto);

        checkCouponState(coupon);

        return coupon.toDto();
    }

    private void checkCouponState(Coupon coupon) {
        CouponState prevState = coupon.getState();
        coupon.updateStateAt(LocalDateTime.now());

        if(coupon.getState() != prevState) {
            couponRepository.save(coupon);
        }
    }

    public Coupon getValidCouponOrThrow(Long couponId) {
        Coupon coupon = findByIdOrElseThrow(couponId);

        coupon.updateStateAt(LocalDateTime.now());

        if(coupon.getState() != CouponState.VALID) {
            throw new BadRequestException(ErrorMessage.INVALID_EVENT_PERIOD.getMessage());
        }

        return coupon;
    }

    public Coupon findByIdOrElseThrow(Long couponId) {
        return couponRepository.findById(couponId)
                .orElseThrow(() -> new BadRequestException(ErrorMessage.EVENT_NOT_FOUND.getMessage()));
    }
}

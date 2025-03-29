package com.example.eightyage.domain.coupon.service;

import com.example.eightyage.domain.coupon.dto.response.IssuedCouponResponseDto;
import com.example.eightyage.domain.coupon.entity.IssuedCoupon;
import com.example.eightyage.domain.coupon.repository.IssuedCouponRepository;
import com.example.eightyage.domain.coupon.entity.Coupon;
import com.example.eightyage.domain.coupon.status.Status;
import com.example.eightyage.domain.user.entity.User;
import com.example.eightyage.global.dto.AuthUser;
import com.example.eightyage.global.exception.BadRequestException;
import com.example.eightyage.global.exception.ErrorMessage;
import com.example.eightyage.global.exception.ForbiddenException;
import com.example.eightyage.global.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class IssuedCouponService {

    private final IssuedCouponRepository issuedCouponRepository;
    private final CouponService couponService;
    private final StringRedisTemplate stringRedisTemplate;

    private static final String EVENT_QUANTITIY_PREFIX = "event:quantity:";
    private static final String EVENT_LOCK_PREFIX = "event:lock:";
    private final RedissonClient redissonClient;

    public IssuedCouponResponseDto issueCoupon(AuthUser authUser, Long couponId) {

        RLock rLock = redissonClient.getLock(EVENT_LOCK_PREFIX + couponId);
        boolean isLocked = false;

        try {
            isLocked = rLock.tryLock(3, 10, TimeUnit.SECONDS);  // 3초 안에 락을 획득, 10초 뒤에는 자동 해제

            if (!isLocked) {
                throw new BadRequestException(ErrorMessage.CAN_NOT_ACCESS.getMessage()); // 락 획득 실패
            }

            Coupon coupon = couponService.getValidCouponOrThrow(couponId);

            if (issuedCouponRepository.existsByUserIdAndCouponId(authUser.getUserId(), couponId)) {
                throw new BadRequestException(ErrorMessage.COUPON_ALREADY_ISSUED.getMessage());
            }

            Long remain = Long.parseLong(stringRedisTemplate.opsForValue().get(EVENT_QUANTITIY_PREFIX + couponId));
            if (remain == 0 || remain < 0) {
                throw new BadRequestException(ErrorMessage.COUPON_OUT_OF_STOCK.getMessage());
            }
            stringRedisTemplate.opsForValue().decrement(EVENT_QUANTITIY_PREFIX + couponId);

            // 쿠폰 발급 및 저장
            IssuedCoupon issuedCoupon = IssuedCoupon.create(User.fromAuthUser(authUser), coupon);
            issuedCouponRepository.save(issuedCoupon);

            return issuedCoupon.toDto();

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new BadRequestException(ErrorMessage.INTERNAL_SERVER_ERROR.getMessage());
        } finally {
            if (isLocked) {
                rLock.unlock();
            }
        }
    }

    public Page<IssuedCouponResponseDto> getMyCoupons(AuthUser authUser, int page, int size) {
        Pageable pageable = PageRequest.of(page-1, size);
        Page<IssuedCoupon> coupons = issuedCouponRepository.findAllByUserIdAndStatus(authUser.getUserId(), Status.VALID, pageable);

        return coupons.map(IssuedCoupon::toDto);
    }

    public IssuedCouponResponseDto getCoupon(AuthUser authUser, Long issuedCouponId) {
        IssuedCoupon issuedCoupon = findByIdOrElseThrow(issuedCouponId);

        if(!issuedCoupon.getUser().equals(User.fromAuthUser(authUser))) {
            throw new ForbiddenException(ErrorMessage.COUPON_FORBIDDEN.getMessage());
        }

        if(issuedCoupon.getStatus().equals(Status.INVALID)) {
            throw new BadRequestException(ErrorMessage.COUPON_ALREADY_USED.getMessage());
        }

        return issuedCoupon.toDto();
    }

    public IssuedCoupon findByIdOrElseThrow(Long issuedCouponId) {
        return issuedCouponRepository.findById(issuedCouponId)
                .orElseThrow(() -> new NotFoundException(ErrorMessage.COUPON_NOT_FOUND.getMessage()));
    }
}

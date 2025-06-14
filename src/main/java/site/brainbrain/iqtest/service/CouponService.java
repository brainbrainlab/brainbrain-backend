package site.brainbrain.iqtest.service;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import site.brainbrain.iqtest.controller.dto.CouponResponse;
import site.brainbrain.iqtest.domain.Coupon;
import site.brainbrain.iqtest.exception.CouponException;
import site.brainbrain.iqtest.repository.CouponRepository;

@RequiredArgsConstructor
@Service
public class CouponService {

    private final CouponRepository couponRepository;

    @Transactional(readOnly = true)
    public CouponResponse getCouponByCode(final String couponCode) {
        final Coupon coupon = couponRepository.fetchByCode(couponCode);
        return new CouponResponse(coupon.getType(), coupon.getDiscountRate(), coupon.isAvailable());
    }

    @Transactional
    public void tryConsumeIfPresent(final String coupon) {
        if (coupon == null) {
            return;
        }
        final int updatedRow = couponRepository.markAsUsed(coupon, LocalDateTime.now());
        if (updatedRow == 0) {
            throw new CouponException("이미 사용된 쿠폰이거나 유효하지 않은 쿠폰입니다.");
        }
    }
}

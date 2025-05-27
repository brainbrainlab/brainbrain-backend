package site.brainbrain.iqtest.service;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import site.brainbrain.iqtest.controller.dto.CouponResponse;
import site.brainbrain.iqtest.domain.Coupon;
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
    public boolean isUnavailableCoupon(final String code) {
        final int updatedRow = couponRepository.markAsUsed(code, LocalDateTime.now());
        return updatedRow == 0;
    }
}

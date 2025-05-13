package site.brainbrain.iqtest.service;

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
}

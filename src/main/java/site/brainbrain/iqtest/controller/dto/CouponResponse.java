package site.brainbrain.iqtest.controller.dto;

import site.brainbrain.iqtest.domain.CouponType;

public record CouponResponse(CouponType couponType,
                             int discountRate,
                             boolean isAvailable) {
}

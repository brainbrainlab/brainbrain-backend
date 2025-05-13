package site.brainbrain.iqtest.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import site.brainbrain.iqtest.controller.dto.CouponResponse;
import site.brainbrain.iqtest.domain.Coupon;
import site.brainbrain.iqtest.domain.CouponType;
import site.brainbrain.iqtest.exception.CouponException;
import site.brainbrain.iqtest.repository.CouponRepository;

@SpringBootTest
public class CouponServiceTest {

    @Autowired
    private CouponService couponService;

    @Autowired
    private CouponRepository couponRepository;

    @Test
    @DisplayName("쿠폰 코드로 쿠폰의 타입, 할인율, 사용 가능 여부를 조회한다.")
    void get_coupon() {
        // given
        final String couponCode = "123456";
        final CouponType type = CouponType.FUNDING;
        final int discountRate = 30;
        final boolean isAvailable = true;

        final Coupon coupon = Coupon.builder()
                .code(couponCode)
                .type(type)
                .discountRate(discountRate)
                .isAvailable(isAvailable)
                .build();
        couponRepository.save(coupon);

        // when
        final CouponResponse couponResponse = couponService.getCouponByCode(couponCode);

        // then
        assertAll(() -> assertThat(couponResponse.couponType()).isEqualTo(type),
                () -> assertThat(couponResponse.discountRate()).isEqualTo(discountRate),
                () -> assertThat(couponResponse.isAvailable()).isEqualTo(isAvailable));
    }

    @Test
    @DisplayName("코드와 일치하는 쿠폰이 없으면 예외가 발생한다.")
    void throw_exception_when_coupon_not_exist() {
        assertThatThrownBy(() -> couponService.getCouponByCode("none"))
                .isInstanceOf(CouponException.class)
                .hasMessage("쿠폰을 찾을 수 없습니다.");
    }
}

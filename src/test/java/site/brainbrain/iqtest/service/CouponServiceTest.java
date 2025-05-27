package site.brainbrain.iqtest.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
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
class CouponServiceTest {

    private static final String TEST_COUPON_CODE = "TEST_COUPON";
    private static final CouponType COUPON_TYPE = CouponType.COMMON;
    private static final int DISCOUNT_RATE = 10;
    private static final boolean IS_AVAILABLE = true;

    @Autowired
    private CouponService couponService;

    @Autowired
    private CouponRepository couponRepository;

    @BeforeEach
    void setUp() {
        final Coupon coupon = Coupon.builder()
                .code(TEST_COUPON_CODE)
                .type(COUPON_TYPE)
                .discountRate(DISCOUNT_RATE)
                .isAvailable(IS_AVAILABLE)
                .build();
        couponRepository.save(coupon);
    }

    @AfterEach
    void clean() {
        couponRepository.deleteAll();
    }

    @Test
    @DisplayName("쿠폰 코드로 쿠폰의 타입, 할인율, 사용 가능 여부를 조회한다.")
    void get_coupon() {
        // given & when
        final CouponResponse couponResponse = couponService.getCouponByCode(TEST_COUPON_CODE);

        // then
        assertAll(() -> assertThat(couponResponse.couponType()).isEqualTo(COUPON_TYPE),
                () -> assertThat(couponResponse.discountRate()).isEqualTo(DISCOUNT_RATE),
                () -> assertThat(couponResponse.isAvailable()).isEqualTo(IS_AVAILABLE));
    }

    @Test
    @DisplayName("코드와 일치하는 쿠폰이 없으면 예외가 발생한다.")
    void throw_exception_when_coupon_not_exist() {
        assertThatThrownBy(() -> couponService.getCouponByCode("none"))
                .isInstanceOf(CouponException.class)
                .hasMessage("쿠폰을 찾을 수 없습니다.");
    }


    @DisplayName("동시에 여러 요청이 들어와도 쿠폰은 한 번만 사용된다")
    @Test
    void only_one_thread_can_use_coupon_when_many_requests() throws InterruptedException {
        // given
        final ExecutorService executor = Executors.newFixedThreadPool(100);
        final CountDownLatch latch = new CountDownLatch(100);

        final AtomicInteger successCount = new AtomicInteger();
        final AtomicInteger failCount = new AtomicInteger();

        final Runnable task = () -> {
            if (couponService.isUnavailableCoupon(TEST_COUPON_CODE)) {
                failCount.incrementAndGet();
            } else {
                successCount.incrementAndGet();
            }
            latch.countDown();
        };

        try {
            // when
            for (int i = 0; i < 100; i++) {
                executor.execute(task);
            }
            latch.await();

            // then
            assertThat(successCount.get()).isEqualTo(1);
            assertThat(failCount.get()).isEqualTo(99);
        } finally {
            executor.shutdown();
        }
    }
}

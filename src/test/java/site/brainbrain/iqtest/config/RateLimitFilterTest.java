package site.brainbrain.iqtest.config;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import static site.brainbrain.iqtest.config.CaffeineBucketRateLimiter.MAX_REQUESTS_PER_MINUTE;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

import site.brainbrain.iqtest.domain.Coupon;
import site.brainbrain.iqtest.repository.CouponRepository;

@SpringBootTest
@AutoConfigureMockMvc
@Import(TestConfig.class)
class RateLimitFilterTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CouponRepository couponRepository;

    private static final String COUPON_ENDPOINT = "/coupons?code=TEST123";
    private static final String TEST_COUPON_CODE = "TEST123";

    @BeforeEach
    void setUp() {
        final Coupon coupon = Coupon.builder()
                .code(TEST_COUPON_CODE)
                .build();
        couponRepository.save(coupon);
    }

    @AfterEach
    void tearDown() {
        couponRepository.deleteAll();
    }

    @Test
    @DisplayName("허용된 요청 수까지는 200 응답 후 이후 요청은 429 상태코드를 반환한다")
    void allowUpToLimit() throws Exception {
        for (int i = 0; i < MAX_REQUESTS_PER_MINUTE; i++) {
            mockMvc.perform(get(COUPON_ENDPOINT)).andExpect(status().isOk());
        }
        mockMvc.perform(get(COUPON_ENDPOINT))
                .andExpect(status().isTooManyRequests())
                .andExpect(content().string("요청 수를 초과했습니다. 잠시 후 다시 시도해주세요."));
        ;
    }

    @Test
    @DisplayName("/coupons 외의 요청은 제한이 걸리지 않는다")
    void otherUriShouldNotBeRateLimited() throws Exception {
        for (int i = 0; i < MAX_REQUESTS_PER_MINUTE + 1; i++) {
            mockMvc.perform(get("/check")).andExpect(status().isOk());
        }
    }

    @Test
    @DisplayName("각 ip 마다 별도로 요청 제한이 적용된다")
    void rateLimitIsAppliedPerIp() throws Exception {
        // given
        final String ip1 = "1.1.1.1";
        final String ip2 = "2.2.2.2";

        for (int i = 0; i < MAX_REQUESTS_PER_MINUTE; i++) {
            mockMvc.perform(get(COUPON_ENDPOINT).with(request -> {
                request.setRemoteAddr(ip1);
                return request;
            }));
        }

        // when & then
        mockMvc.perform(get(COUPON_ENDPOINT).with(request -> {
            request.setRemoteAddr(ip2);
            return request;
        })).andExpect(status().isOk());
    }
}

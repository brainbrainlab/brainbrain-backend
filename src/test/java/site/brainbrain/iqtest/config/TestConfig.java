package site.brainbrain.iqtest.config;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

import site.brainbrain.iqtest.fake.FakeIpRateLimiter;

@TestConfiguration
public class TestConfig {

    @Bean
    @Primary
    public IpRateLimiter fakeRateLimiter() {
        return new FakeIpRateLimiter();
    }
}

package site.brainbrain.iqtest.fake;

import static site.brainbrain.iqtest.config.CaffeineBucketRateLimiter.MAX_REQUESTS_PER_MINUTE;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import site.brainbrain.iqtest.config.IpRateLimiter;

public class FakeIpRateLimiter implements IpRateLimiter {

    private final ConcurrentHashMap<String, AtomicInteger> requestCounts = new ConcurrentHashMap<>();

    @Override
    public boolean isRateLimited(final String ip) {
        final int requestCount = requestCounts
                .computeIfAbsent(ip, key -> new AtomicInteger(0))
                .incrementAndGet();
        return requestCount > MAX_REQUESTS_PER_MINUTE;
    }
}

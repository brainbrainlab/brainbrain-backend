package site.brainbrain.iqtest.config;

import java.time.Duration;

import org.springframework.stereotype.Component;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Refill;

@Component
public class CaffeineBucketRateLimiter implements IpRateLimiter {

    public static final int MAX_REQUESTS_PER_MINUTE = 10;
    private static final int CACHE_EXPIRE_MINUTE = 10;
    private static final int CACHE_MAX_SIZE = 300;

    private final Cache<String, Bucket> ipBucketCache = Caffeine.newBuilder()
            .expireAfterAccess(Duration.ofMinutes(CACHE_EXPIRE_MINUTE))
            .maximumSize(CACHE_MAX_SIZE)
            .build();

    @Override
    public boolean isRateLimited(final String ip) {
        final Bucket bucket = ipBucketCache.get(ip, this::newBucket);
        return !bucket.tryConsume(1);
    }

    private Bucket newBucket(final String ip) {
        final Refill refill = Refill.greedy(MAX_REQUESTS_PER_MINUTE, Duration.ofMinutes(1));
        final Bandwidth limit = Bandwidth.classic(MAX_REQUESTS_PER_MINUTE, refill);
        return Bucket.builder().addLimit(limit).build();
    }
}

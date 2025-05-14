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

    private final Cache<String, Bucket> ipBucketCache = Caffeine.newBuilder()
            .expireAfterAccess(Duration.ofMinutes(10))
            .maximumSize(300)
            .build();

    @Override
    public boolean isRateLimited(final String ip) {
        final Bucket bucket = ipBucketCache.get(ip, this::newBucket);
        return !bucket.tryConsume(1);
    }

    private Bucket newBucket(final String ip) {
        final Refill refill = Refill.greedy(10, Duration.ofMinutes(1));
        final Bandwidth limit = Bandwidth.classic(10, refill);
        return Bucket.builder().addLimit(limit).build();
    }
}

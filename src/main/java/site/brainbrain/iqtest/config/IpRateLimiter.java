package site.brainbrain.iqtest.config;

public interface IpRateLimiter {

    boolean isRateLimited(final String ip);
}

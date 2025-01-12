package cn.edu.nwafu.nexus.infrastructure.cache.redis;

import java.util.concurrent.TimeUnit;

/**
 * @author Huang Z.Y.
 */
public enum CacheKeyEnums {
    /**
     * Redis各类缓存集合
     */
    LOGIN_USER_KEY("login_tokens:", 30, TimeUnit.MINUTES),
    RATE_LIMIT_KEY("rate_limit:", 60, TimeUnit.SECONDS),
    USER_KEY("user:", 60, TimeUnit.MINUTES),
    ROLE_KEY("role:", 60, TimeUnit.MINUTES),
    POST_KEY("post:", 60, TimeUnit.MINUTES);

    private final String key;
    private final int expiration;
    private final TimeUnit timeUnit;

    CacheKeyEnums(String key, int expiration, TimeUnit timeUnit) {
        this.key = key;
        this.expiration = expiration;
        this.timeUnit = timeUnit;
    }

    public String key() {
        return key;
    }

    public int expiration() {
        return expiration;
    }

    public TimeUnit timeUnit() {
        return timeUnit;
    }
}

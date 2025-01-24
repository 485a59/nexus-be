package cn.edu.nwafu.nexus.infrastructure.cache.redis;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.Nonnull;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

/**
 * Redis缓存模版，三级缓存
 *
 * @author Huang Z.Y.
 */
@SuppressWarnings("unchecked")
@Slf4j
public class RedisCacheTemplate<T> {
    private final LoadingCache<String, Optional<T>> guavaCache;
    private final CacheKeyEnums cacheKey;
    private final RedisUtils redisUtils;

    public RedisCacheTemplate(CacheKeyEnums cacheKey, RedisUtils redisUtils) {
        this.cacheKey = cacheKey;
        this.redisUtils = redisUtils;
        this.guavaCache = CacheBuilder.newBuilder()
                // 基于容量回收，缓存的最大数量，超过就取 MAXIMUM_CAPACITY = 1 << 30。依靠 LRU 队列 recencyQueue 来进行容量淘汰
                .maximumSize(1024)
                .softValues()
                // 没写访问下，超过 5 秒会失效(非自动失效，需有任意 put get 方法才会扫描过期失效数据。
                // 但区别是会开一个异步线程进行刷新，刷新过程中访问返回旧数据)
                .expireAfterWrite(cacheKey.expiration(), cacheKey.timeUnit())
                // 并行等级。决定 segment 数量的参数，concurrencyLevel 与 maxWeight 共同决定
                .concurrencyLevel(64)
                // 所有 segment 的初始总容量大小
                .initialCapacity(128)
                .build(new CacheLoader<String, Optional<T>>() {
                    @Override
                    public Optional<T> load(@Nonnull String cachedKey) {
                        T cacheObject = (T) redisUtils.get(cachedKey);
                        log.debug("find the redis cache of key: {} is {}", cachedKey, cacheObject);
                        return Optional.ofNullable(cacheObject);
                    }
                });
    }

    /**
     * 从缓存中获取对象
     *
     * @param id id
     */
    public T get(Object id) {
        String cachedKey = genKey(id);
        try {
            Optional<T> optional = guavaCache.get(cachedKey);
            log.debug("查找Guava缓存: {}", cachedKey);
            return optional.orElse(null);
        } catch (ExecutionException e) {
            log.error("从缓存中获取对象失败", e);
            return null;
        }
    }

    /**
     * 仅从缓存中获取对象
     */
    public T list() {
        try {
            String key = genKey("*");
            Optional<T> optional = guavaCache.get(key);
            return optional.orElse(null);
        } catch (ExecutionException e) {
            log.error("从缓存中获取对象失败", e);
            return null;
        }
    }


    public void set(Object id, T obj) {
        redisUtils.set(genKey(id), obj, cacheKey.expiration(), cacheKey.timeUnit());
        guavaCache.refresh(genKey(id));
    }

    public void delete(Object id) {
        redisUtils.del(genKey(id));
        guavaCache.refresh(genKey(id));
    }

    public void refresh(Object id) {
        redisUtils.expire(genKey(id), cacheKey.expiration(), cacheKey.timeUnit());
        guavaCache.refresh(genKey(id));
    }

    public String genKey(Object id) {
        return cacheKey.key() + id;
    }

    public T getFromDb(Object id) {
        return null;
    }
}
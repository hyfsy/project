package com.hyf.cache.impl.support.redis;

import java.time.Duration;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;

import org.springframework.cache.support.NullValue;
import org.springframework.cache.support.SimpleValueWrapper;
import org.springframework.core.convert.ConversionService;
import org.springframework.data.redis.cache.RedisCache;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheWriter;

import com.hyf.cache.Cache;
import com.hyf.cache.enums.CacheType;
import com.hyf.cache.impl.context.DynamicCacheConfigContext;
import com.hyf.cache.impl.key.generator.KeyGenerateManager;
import com.hyf.cache.impl.utils.LockUtils;

/**
 * 适配原生redis缓存，提供ttl和全局锁支持
 *
 * @author baB_hyf
 * @date 2022/02/10
 * @see AdaptiveRedisCacheManager
 */
public class AdaptiveRedisCache extends RedisCache implements Cache
{

    /**
     * Create new {@link RedisCache}.
     *
     * @param name
     *            must not be {@literal null}.
     * @param cacheWriter
     *            must not be {@literal null}.
     * @param cacheConfig
     *            must not be {@literal null}.
     */
    protected AdaptiveRedisCache(String name, RedisCacheWriter cacheWriter, RedisCacheConfiguration cacheConfig) {
        super(name, cacheWriter, cacheConfig);
    }

    @Override
    public void put(Object key, Object value) {
        Long ttl = DynamicCacheConfigContext.getTTL();
        TimeUnit unit = DynamicCacheConfigContext.getTTLUnit();
        put(key, value, ttl, unit);
    }

    @Override
    public ValueWrapper putIfAbsent(Object key, Object value) {
        Long ttl = DynamicCacheConfigContext.getTTL();
        TimeUnit unit = DynamicCacheConfigContext.getTTLUnit();
        return putIfAbsent(key, value, ttl, unit);
    }

    @Override
    public void put(Object key, Object value, long ttl, TimeUnit unit) {
        put(key, value, ttl, unit, 0);
    }

    @Override
    public void put(Object key, Object value, long ttl, TimeUnit unit, long randomMillis) {
        if (notAllowNull(value)) {
            return;
        }

        value = value == null ? NullValue.INSTANCE : value;
        getNativeCache().put(getName(), convertCacheKey(key), convertCacheValue(value),
                getDuration(ttl, unit, randomMillis));
    }

    @Override
    public ValueWrapper putIfAbsent(Object key, Object value, long ttl, TimeUnit unit) {
        return putIfAbsent(key, value, ttl, unit, 0);
    }

    @Override
    public ValueWrapper putIfAbsent(Object key, Object value, long ttl, TimeUnit unit, long randomMillis) {
        if (notAllowNull(value)) {
            return get(key);
        }

        value = value == null ? NullValue.INSTANCE : value;
        return convertCacheResult(getNativeCache().putIfAbsent(getName(), convertCacheKey(key),
                convertCacheValue(value), getDuration(ttl, unit, randomMillis)));
    }

    @Override
    public void touch(Object key, long ttl, TimeUnit unit) {
        get(key);
    }

    @Override
    public ValueWrapper getAll() {
        // TODO
        throw new UnsupportedOperationException();
    }

    @Override
    public CacheType getCacheType() {
        return CacheType.REDIS;
    }

    @Override
    public Lock getLock(Object lockName) {
        return LockUtils.getRedisLock(lockName);
    }

    @Override
    public ReadWriteLock getReadWriteLock(Object lockName) {
        return LockUtils.getRedisReadWriteLock(lockName);
    }

    @Override
    protected String createCacheKey(Object key) {
        // not support prefix key
        return convertKey(key);
    }

    @Override
    public void clear() {

        List<Object> cacheKeyPatterns = KeyGenerateManager.getAllCacheKeyPattern(getName());

        RedisCacheWriter writer = getNativeCache();
        ConversionService conversionService = getCacheConfiguration().getConversionService();
        for (Object pattern : cacheKeyPatterns) {
            byte[] key = conversionService.convert(pattern, byte[].class);
            writer.clean(getName(), key);
        }
    }

    protected boolean notAllowNull(Object value) {
        if (value != null) {
            return false;
        }

        Boolean allowNull = DynamicCacheConfigContext.getAllowNull();
        if (allowNull != null && !allowNull) {
            return true;
        }

        return !isAllowNullValues();
    }

    protected byte[] convertCacheKey(Object key) {
        return serializeCacheKey(createCacheKey(key));
    }

    protected byte[] convertCacheValue(Object value) {
        return serializeCacheValue(value);
    }

    protected ValueWrapper convertCacheResult(byte[] result) {
        if (result == null) {
            return null;
        }
        return new SimpleValueWrapper(fromStoreValue(deserializeCacheValue(result)));
    }

    private Duration getDuration(Long ttl, TimeUnit unit, Long randomMillis) {
        if (ttl != null && ttl < 0) {
            return null;
        }

        Duration duration = getCacheConfiguration().getTtl();
        if (ttl != null && unit != null) {
            duration = Duration.ofMillis(unit.toMillis(ttl));
        }

        if (duration.isNegative() || duration.isZero()) {
            return duration;
        }

        if (unit != null && randomMillis != null && randomMillis > 0) {
            duration = duration.plus(Duration.ofMillis(randomMillis));
        }

        return duration;
    }
}

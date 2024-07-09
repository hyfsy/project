package com.hyf.cache.impl.support;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.function.BiFunction;

import com.hyf.cache.Cache;
import com.hyf.cache.enums.CacheType;
import com.hyf.cache.impl.context.DynamicCacheConfigContext;

/**
 * 根据方法传入的ttl，动态查找对应ttl的缓存，没有就根据需要创建
 * 
 * @author baB_hyf
 * @date 2022/02/17
 */
public class DynamicTTLCache implements Cache
{

    /** 默认缓存key */
    private static final Duration DEFAULT_DURATION = Duration.ZERO;

    /** ttl -> cache */
    private final Map<Duration, Cache> caches = new ConcurrentHashMap<>(32);

    /** 进行缓存的创建工作 */
    private final BiFunction<Long, TimeUnit, Cache> cacheLoader;

    public DynamicTTLCache(Cache defaultCache, BiFunction<Long, TimeUnit, Cache> cacheLoader) {
        this.cacheLoader = cacheLoader;
        caches.put(DEFAULT_DURATION, defaultCache);
    }

    private Cache getDefaultCache() {
        return caches.get(DEFAULT_DURATION);
    }

    /**
     * 通过ttl获取缓存
     *
     * @param ttl
     *            ttl数量
     * @param unit
     *            ttl单位
     * @return 缓存对象，或null
     */
    private Cache getCacheByDuration(long ttl, TimeUnit unit) {
        if (ttl <= 0 || unit == null) {
            return getDefaultCache();
        }
        Duration duration = Duration.ofMillis(unit.toMillis(ttl));
        return caches.computeIfAbsent(duration, d -> cacheLoader.apply(ttl, unit));
    }

    @Override
    public void put(Object key, Object value, long ttl, TimeUnit unit) {
        put(key, value, ttl, unit, 0);
    }

    @Override
    public void put(Object key, Object value, long ttl, TimeUnit unit, long randomMillis) {
        getCacheByDuration(ttl, unit).put(key, value, ttl, unit, randomMillis);
    }

    @Override
    public ValueWrapper putIfAbsent(Object key, Object value, long ttl, TimeUnit unit) {
        return putIfAbsent(key, value, ttl, unit, 0);
    }

    @Override
    public ValueWrapper putIfAbsent(Object key, Object value, long ttl, TimeUnit unit, long randomMillis) {
        return getCacheByDuration(ttl, unit).putIfAbsent(key, value, ttl, unit, randomMillis);
    }

    @Override
    public void touch(Object key, long ttl, TimeUnit unit) {
        getCacheByDuration(ttl, unit).touch(key, ttl, unit);
    }

    @Override
    public ValueWrapper getAll() {
        List<Object> values = new ArrayList<>();
        caches.values().forEach(c -> values.add(c.getAll().get()));
        return () -> values;
    }

    @Override
    public CacheType getCacheType() {
        return getDefaultCache().getCacheType();
    }

    @Override
    public Lock getLock(Object lockName) {
        return getDefaultCache().getLock(lockName);
    }

    @Override
    public ReadWriteLock getReadWriteLock(Object lockName) {
        return getDefaultCache().getReadWriteLock(lockName);
    }

    @Override
    public String getName() {
        return getDefaultCache().getName();
    }

    @Override
    public Object getNativeCache() {
        return caches;
    }

    @Override
    public ValueWrapper get(Object key) {
        for (Cache cache : caches.values()) {
            ValueWrapper v = cache.get(key);
            if (v != null) {
                return v;
            }
        }
        return null;
    }

    @Override
    public <T> T get(Object key, Class<T> type) {
        for (Cache cache : caches.values()) {
            T v = cache.get(key, type);
            if (v != null) {
                return v;
            }
        }
        return null;
    }

    @Override
    public <T> T get(Object key, Callable<T> valueLoader) {
        for (Cache cache : caches.values()) {
            T v = cache.get(key, valueLoader);
            if (v != null) {
                return v;
            }
        }
        return null;
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
    public void evict(Object key) {
        for (Cache cache : caches.values()) {
            cache.evict(key);
        }
    }

    @Override
    public void clear() {
        for (Cache cache : caches.values()) {
            cache.clear();
        }
    }
}

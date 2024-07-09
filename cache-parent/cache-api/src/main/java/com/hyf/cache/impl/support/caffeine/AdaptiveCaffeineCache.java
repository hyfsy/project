package com.hyf.cache.impl.support.caffeine;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;

import org.springframework.cache.caffeine.CaffeineCache;

import com.hyf.cache.Cache;
import com.hyf.cache.enums.CacheType;
import com.hyf.cache.impl.utils.LockUtils;

/**
 * 适配原生caffeine缓存，提供ttl和全局锁支持
 * 
 * @author baB_hyf
 * @date 2022/02/17
 * @see AdaptiveCaffeineCacheManager
 * @see com.hyf.cache.impl.support.DynamicTTLCache
 */
public class AdaptiveCaffeineCache extends CaffeineCache implements Cache
{

    public AdaptiveCaffeineCache(String name, com.github.benmanes.caffeine.cache.Cache<Object, Object> cache) {
        super(name, cache);
    }

    public AdaptiveCaffeineCache(String name, com.github.benmanes.caffeine.cache.Cache<Object, Object> cache, boolean allowNullValues) {
        super(name, cache, allowNullValues);
    }

    @Override
    public void put(Object key, Object value) {
        super.put(key, value);
    }

    @Override
    public ValueWrapper putIfAbsent(Object key, Object value) {
        return super.putIfAbsent(key, value);
    }

    @Override
    public void put(Object key, Object value, long ttl, TimeUnit unit) {
        super.put(key, value);
    }

    @Override
    public void put(Object key, Object value, long ttl, TimeUnit unit, long randomMillis) {
        //
        super.put(key, value);
    }

    @Override
    public ValueWrapper putIfAbsent(Object key, Object value, long ttl, TimeUnit unit) {
        return super.putIfAbsent(key, value);
    }

    @Override
    public ValueWrapper putIfAbsent(Object key, Object value, long ttl, TimeUnit unit, long randomMillis) {
        return super.putIfAbsent(key, value);
    }

    @Override
    public void touch(Object key, long ttl, TimeUnit unit) {
        get(key);
    }

    @Override
    public ValueWrapper getAll() {
        return () -> getNativeCache().asMap();
    }

    @Override
    public CacheType getCacheType() {
        return CacheType.CAFFEINE;
    }

    @Override
    public Lock getLock(Object lockName) {
        return LockUtils.getJvmLock(getCacheType().toString(), lockName);
    }

    @Override
    public ReadWriteLock getReadWriteLock(Object lockName) {
        return LockUtils.getJvmReadWriteLock(getCacheType().toString(), lockName);
    }
}

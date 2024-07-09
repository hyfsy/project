package com.hyf.cache;

import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;

import com.hyf.cache.enums.CacheType;

/**
 * 缓存装饰抽象类
 * 
 * @author baB_hyf
 * @date 2022/02/17
 * @see Cache
 */
public abstract class AbstractCacheDecorator implements Cache
{

    protected Cache cache;

    public AbstractCacheDecorator(Cache cache) {
        this.cache = cache;
    }

    @Override
    public void put(Object key, Object value, long ttl, TimeUnit unit) {
        cache.put(key, value, ttl, unit);
    }

    @Override
    public void put(Object key, Object value, long ttl, TimeUnit unit, long randomMillis) {
        cache.put(key, value, ttl, unit, randomMillis);
    }

    @Override
    public ValueWrapper putIfAbsent(Object key, Object value, long ttl, TimeUnit unit) {
        return cache.putIfAbsent(key, value, ttl, unit);
    }

    @Override
    public ValueWrapper putIfAbsent(Object key, Object value, long ttl, TimeUnit unit, long randomMillis) {
        return cache.putIfAbsent(key, value, ttl, unit, randomMillis);
    }

    @Override
    public void touch(Object key, long ttl, TimeUnit unit) {
        cache.touch(key, ttl, unit);
    }

    @Override
    public ValueWrapper getAll() {
        return cache.getAll();
    }

    @Override
    public CacheType getCacheType() {
        return cache.getCacheType();
    }

    @Override
    public Lock getLock(Object lockName) {
        return cache.getLock(lockName);
    }

    @Override
    public ReadWriteLock getReadWriteLock(Object lockName) {
        return cache.getReadWriteLock(lockName);
    }

    @Override
    public String getName() {
        return cache.getName();
    }

    @Override
    public Object getNativeCache() {
        return cache.getNativeCache();
    }

    @Override
    public ValueWrapper get(Object key) {
        return cache.get(key);
    }

    @Override
    public <T> T get(Object key, Class<T> type) {
        return cache.get(key, type);
    }

    @Override
    public <T> T get(Object key, Callable<T> valueLoader) {
        return cache.get(key, valueLoader);
    }

    @Override
    public void put(Object key, Object value) {
        cache.put(key, value);
    }

    @Override
    public ValueWrapper putIfAbsent(Object key, Object value) {
        return cache.putIfAbsent(key, value);
    }

    @Override
    public void evict(Object key) {
        cache.evict(key);
    }

    @Override
    public boolean evictIfPresent(Object key) {
        return cache.evictIfPresent(key);
    }

    @Override
    public void clear() {
        cache.clear();
    }

    @Override
    public boolean invalidate() {
        return cache.invalidate();
    }
}

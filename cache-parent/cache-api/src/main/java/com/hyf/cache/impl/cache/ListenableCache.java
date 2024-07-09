package com.hyf.cache.impl.cache;

import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

import com.hyf.cache.AbstractCacheDecorator;
import com.hyf.cache.Cache;
import com.hyf.cache.enums.CacheEventType;

/**
 * 添加缓存事件功能
 *
 * @author baB_hyf
 * @date 2022/01/21
 * @see com.hyf.cache.CacheListener
 * @see CacheEventPublisher
 */
public class ListenableCache extends AbstractCacheDecorator
{

    public ListenableCache(Cache cache) {
        super(cache);
    }

    @Override
    public void put(Object key, Object value, long ttl, TimeUnit unit) {
        super.put(key, value, ttl, unit);
        CacheEventPublisher.publishEvent(CacheEventType.MODIFY, getName(), getCacheType(), key, null, value);
    }

    @Override
    public void put(Object key, Object value, long ttl, TimeUnit unit, long randomMillis) {
        super.put(key, value, ttl, unit, randomMillis);
        CacheEventPublisher.publishEvent(CacheEventType.MODIFY, getName(), getCacheType(), key, null, value);
    }

    @Override
    public ValueWrapper putIfAbsent(Object key, Object value, long ttl, TimeUnit unit) {
        ValueWrapper valueWrapper = super.putIfAbsent(key, value, ttl, unit);
        // TODO
        CacheEventPublisher.publishEvent(CacheEventType.CREATE, getName(), getCacheType(), key, null, value);
        return valueWrapper;
    }

    @Override
    public ValueWrapper putIfAbsent(Object key, Object value, long ttl, TimeUnit unit, long randomMillis) {
        ValueWrapper valueWrapper = super.putIfAbsent(key, value, ttl, unit, randomMillis);
        // TODO
        CacheEventPublisher.publishEvent(CacheEventType.CREATE, getName(), getCacheType(), key, null, value);
        return valueWrapper;
    }

    @Override
    public ValueWrapper get(Object key) {
        ValueWrapper valueWrapper = super.get(key);
        if (valueWrapper != null) {
            CacheEventPublisher.publishEvent(CacheEventType.HIT, getName(), getCacheType(), key, null,
                    valueWrapper.get());
        }
        return valueWrapper;
    }

    @Override
    public <T> T get(Object key, Class<T> type) {
        T t = super.get(key, type);
        if (t != null) {
            CacheEventPublisher.publishEvent(CacheEventType.HIT, getName(), getCacheType(), key, null, t);
        }
        return t;
    }

    @Override
    public <T> T get(Object key, Callable<T> valueLoader) {
        T t = super.get(key, valueLoader);
        if (t != null) {
            CacheEventPublisher.publishEvent(CacheEventType.HIT, getName(), getCacheType(), key, null, t);
        }
        return t;
    }

    @Override
    public void put(Object key, Object value) {
        super.put(key, value);
        CacheEventPublisher.publishEvent(CacheEventType.CREATE, getName(), getCacheType(), key, null, value);
    }

    @Override
    public void evict(Object key) {
        super.evict(key);
        CacheEventPublisher.publishEvent(CacheEventType.EVICT, getName(), getCacheType(), key, null, null);
    }

    @Override
    public void clear() {
        ValueWrapper all = getAll();
        super.clear();
        CacheEventPublisher.publishEvent(CacheEventType.EVICT, getName(), getCacheType(), null, all.get(), null);
    }
}

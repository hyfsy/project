package com.hyf.cache;

import com.hyf.cache.enums.CacheType;

/**
 * 缓存相关事件的监听器，监听多种缓存事件
 * 
 * @author baB_hyf
 * @date 2022/01/14
 */
public interface CacheListener
{

    boolean interest(CacheType cacheType, String cacheName, Object cacheKey);

    default void onHit(CacheEvent event) {
    }

    default void onCreated(CacheEvent event) {
    }

    default void onModified(CacheEvent event) {
    }

    // TODO 类库缓存自动evicted过期需要对接
    default void onEvicted(CacheEvent event) {
    }

    // TODO 类库缓存自动expired过期需要对接
    default void onExpired(CacheEvent event) {
    }

    default boolean isGlobal() {
        return true;
    }
}

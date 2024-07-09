package com.hyf.cache;

import java.util.concurrent.TimeUnit;

import org.springframework.context.ApplicationEvent;

import com.hyf.cache.enums.CacheEventType;
import com.hyf.cache.enums.CacheType;

/**
 * 缓存处理的相关事件
 * 
 * @author baB_hyf
 * @date 2022/01/14
 */
public class CacheEvent extends ApplicationEvent
{

    /**
     * 
     */
    private static final long serialVersionUID = -1238022989984000438L;

    private final Object key;
    private final String cacheName;
    private final CacheType cacheType;
    private CacheEventType cacheOperateType;
    private CacheValueWrapper oldValue;
    private CacheValueWrapper newValue;

    // TODO ?
    private long ttl;
    private TimeUnit unit;

    @Override
    public String toString() {
        return "CacheEvent{" + "cacheType=" + cacheType + "cacheName=" + cacheName + ",cacheOperateType=" + cacheOperateType + ", oldValue=" + oldValue + ", newValue=" + newValue + ", ttl=" + ttl + ", unit=" + unit + ", key=" + key + '}';
    }

    public CacheEvent(String cacheName, Object key, CacheType cacheType, CacheEventType cacheOperateType, CacheValueWrapper oldValue, CacheValueWrapper newValue) {
        super(key);
        this.cacheName = cacheName;
        this.key = key;
        this.cacheType = cacheType;
        this.cacheOperateType = cacheOperateType;
        this.oldValue = oldValue;
        this.newValue = newValue;
    }

    public boolean isCreated() {
        return CacheEventType.CREATE == cacheOperateType;
    }

    public boolean isModified() {
        return CacheEventType.MODIFY == cacheOperateType;
    }

    public boolean isEvicted() {
        return CacheEventType.EVICT == cacheOperateType;
    }

    public boolean isExpired() {
        return CacheEventType.EXPIRE == cacheOperateType;
    }

    public Object getKey() {
        return key;
    }

    public Object getOldValue() {
        return oldValue;
    }

    public Object getNewValue() {
        return newValue;
    }

    public long getTtl() {
        return ttl;
    }

    public TimeUnit getUnit() {
        return unit;
    }

    public CacheEventType getCacheOperateType() {
        return cacheOperateType;
    }

    public String getCacheName() {
        return cacheName;
    }

    public CacheType getCacheType() {
        return cacheType;
    }

}

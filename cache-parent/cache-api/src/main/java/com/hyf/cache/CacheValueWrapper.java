package com.hyf.cache;

import org.springframework.cache.support.SimpleValueWrapper;

import com.hyf.cache.enums.CacheType;

/**
 * 缓存结果包装，方便获取缓存相关的信息
 *
 * @author baB_hyf
 * @date 2022/01/16
 */
public class CacheValueWrapper extends SimpleValueWrapper
{

    // 从哪种缓存中获取的
    private final CacheType cacheType;

    public CacheValueWrapper(CacheType cacheType, Object value) {
        super(value);
        this.cacheType = cacheType;
    }

    public CacheType getCacheType() {
        return cacheType;
    }
}

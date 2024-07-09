package com.hyf.cache.impl.index;

import java.util.Set;

import com.hyf.cache.CacheKey;
import com.hyf.cache.enums.CacheType;

/**
 * TODO 基于redis的全局索引缓存
 * 
 * @author baB_hyf
 * @date 2022/02/21
 * @see IndexCacheManager
 */
public class RedisIndexCache implements IndexCache
{

    @Override
    public String getCacheName() {
        return null;
    }

    @Override
    public CacheType getCacheType() {
        return null;
    }

    @Override
    public Set<CacheKey> getAllKeys() {
        return null;
    }

    @Override
    public Set<CacheKey> getAllKeys(String pattern) {
        return null;
    }
}

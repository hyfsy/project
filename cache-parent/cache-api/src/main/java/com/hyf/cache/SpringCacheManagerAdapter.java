package com.hyf.cache;

import java.util.Collection;

import org.springframework.cache.Cache;

/**
 * 缓存管理器{@link CacheManager}的适配器，适配成{@link org.springframework.cache.CacheManager}
 *
 * @author baB_hyf
 * @date 2022/01/14
 * @see org.springframework.cache.CacheManager
 * @see CacheManager
 */
public class SpringCacheManagerAdapter implements org.springframework.cache.CacheManager
{

    private CacheManager cacheManager;
    private Boolean dynamic = null;

    public SpringCacheManagerAdapter(CacheManager cacheManager) {
        this.cacheManager = cacheManager;
    }

    public SpringCacheManagerAdapter(CacheManager cacheManager, boolean dynamic) {
        this.cacheManager = cacheManager;
        this.dynamic = dynamic;
    }

    @Override
    public Cache getCache(String name) {
        if (dynamic == null) {
            return cacheManager.findCache(name);
        }
        else {
            return cacheManager.findCache(name, dynamic);
        }
    }

    @Override
    public Collection<String> getCacheNames() {
        return cacheManager.getCacheNameList();
    }
}

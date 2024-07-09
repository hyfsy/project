package com.hyf.cache.impl.index;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;

import com.hyf.cache.CacheKey;
import com.hyf.cache.enums.CacheType;

/**
 * 基于map的全局索引缓存
 * 
 * @author baB_hyf
 * @date 2022/02/21
 * @see IndexCacheManager
 */
public class MapIndexCache implements IndexCache
{

    public static final PathMatcher pathMatcher = new AntPathMatcher();

    private String cacheName;
    private CacheType cacheType;
    private Map<Object, Object> storage;

    public MapIndexCache(String cacheName, CacheType cacheType, Map<Object, Object> storage) {
        this.cacheName = cacheName;
        this.cacheType = cacheType;
        this.storage = storage;
    }

    @Override
    public String getCacheName() {
        return cacheName;
    }

    @Override
    public CacheType getCacheType() {
        return cacheType;
    }

    @Override
    public Set<CacheKey> getAllKeys() {
        Set<CacheKey> keys = new HashSet<>();
        Set<Object> objects = storage.keySet();
        objects.stream().filter(CacheKey.class::isInstance).map(CacheKey.class::cast).forEach(keys::add);
        return keys;
    }

    @Override
    public Set<CacheKey> getAllKeys(String pattern) {
        Set<CacheKey> keys = new HashSet<>();
        Set<Object> objects = storage.keySet();
        objects.stream().filter(CacheKey.class::isInstance).map(CacheKey.class::cast)
                .filter(k -> pathMatcher.match(pattern, k.toString())).forEach(keys::add);
        return keys;
    }
}

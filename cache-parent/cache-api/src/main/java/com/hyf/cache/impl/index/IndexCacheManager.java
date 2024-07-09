package com.hyf.cache.impl.index;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.hyf.cache.enums.CacheType;

/**
 * 全局索引缓存管理器
 * 
 * @author baB_hyf
 * @date 2022/02/11
 * @see IndexCache
 */
public class IndexCacheManager
{

    // cacheType -> cacheClass, IndexCache
    private static final Map<CacheType, Map<String, IndexCache>> indexCacheMap = new ConcurrentHashMap<>(8);

    public static IndexCache getIndexCache(CacheType cacheType, String cacheName) {
        Map<String, IndexCache> nameMap = indexCacheMap.computeIfAbsent(cacheType, k -> new ConcurrentHashMap<>());
        // TODO
        return nameMap.computeIfAbsent(cacheName, name -> new MapIndexCache(name, cacheType, new HashMap<>()));
    }
}

package com.hyf.cache.impl.index.bus;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author baB_hyf
 * @date 2022/02/11
 */
public class ReuseIndexCacheManager {

    // cacheName -> cacheClass, IndexCache
    private static final Map<String, Map<String, ReuseIndexCache>> indexCacheMap = new ConcurrentHashMap<>(8);

    public Map<String, ReuseIndexCache> getIndexCaches(String cacheName) {
        return null;
    }

    public ReuseIndexCache getIndexCaches(String cacheName, String cacheClassName) {
        return null;
    }
}

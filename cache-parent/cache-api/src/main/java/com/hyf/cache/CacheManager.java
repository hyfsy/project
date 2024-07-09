package com.hyf.cache;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.hyf.cache.enums.CacheType;
import com.hyf.cache.impl.context.DynamicCacheConfigContext;

/**
 * 缓存管理器，主要根据cacheName获取对应的{@link Cache}对象集合
 * 
 * @author baB_hyf
 * @date 2022/01/14
 * @see CacheResolver
 */
public interface CacheManager
{

    /**
     * 获取当前缓存的所有名称列表
     *
     * @return 缓存名称列表
     */
    List<String> getCacheNameList();

    /**
     * 通过名称查找缓存
     *
     * @param cacheName
     *            缓存名称
     * @return 查找到的缓存对象，或null
     */
    Cache findCache(String cacheName);

    /**
     * 获取当前缓存的缓存类型
     *
     * @return 缓存类型
     */
    CacheType getCacheType();

    /**
     * 根据需要动态创建缓存
     *
     * @param cacheName
     *            缓存名称
     * @param create
     *            是否动态创建
     * @return 查找到的缓存对象，或null
     */
    default Cache findCache(String cacheName, boolean create) {
        DynamicCacheConfigContext.setDynamicCreate(create);
        try {
            return CacheAdapter.adapt(findCache(cacheName));
        }
        finally {
            DynamicCacheConfigContext.removeDynamicCreate();
        }
    }

    /**
     * 获取缓存map，cacheName -> Cache对象
     * 
     * @return 当前业务下的所有缓存
     */
    default Map<String, Cache> getCacheMap() {
        Map<String, Cache> cacheMap = new HashMap<>();
        for (String cacheName : getCacheNameList()) {
            Cache cache = findCache(cacheName);
            if (cache != null) {
                cacheMap.put(cacheName, cache);
            }
        }
        return cacheMap;
    }
}

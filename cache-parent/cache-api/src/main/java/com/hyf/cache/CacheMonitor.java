package com.hyf.cache;

import java.util.List;
import java.util.Map;

import com.hyf.cache.enums.CacheType;

/**
 * 缓存监控器，运行时监控系统所有缓存
 * 
 * @author baB_hyf
 * @date 2022/01/14
 * @see CacheManager
 * @see Cache
 * @see CacheKey
 */
public interface CacheMonitor
{

    /**
     * 获取所有缓存名称，cacheType -> list&lt;cacheName&gt;
     * 
     * @return 所有缓存名称
     */
    Map<CacheType, List<String>> getAllCacheName();

    /**
     * 获取所有缓存管理器
     *
     * @return 所有缓存管理器
     */
    List<CacheManager> getAllCacheManager();

    /**
     * 获取所有缓存值，cacheType -> map&lt;cacheName, map&lt;cacheKey, cacheValue&gt;&gt;
     *
     * @return 所有缓存值
     */
    Map<CacheType, Map<String, Map<String, Object>>> getAllCacheValue();

    /**
     * 获取缓存名称列表
     *
     * @param cacheType
     *            缓存类型
     * @return 缓存名称列表
     */
    List<String> getCacheNameList(CacheType cacheType);

    /**
     * 获取缓存管理器
     *
     * @param cacheType
     *            缓存类型
     * @return 缓存管理器
     */
    CacheManager getCacheManager(CacheType cacheType);

    /**
     * 获取缓存结果map
     *
     * @param cacheManager
     *            缓存管理器
     * @return 缓存结果
     */
    Map<String, Map<String, Object>> getCacheValueMap(CacheManager cacheManager);

    /**
     * 获取缓存结果map
     *
     * @param cacheManager
     *            缓存管理器
     * @param cacheName
     *            缓存名称
     * @return 缓存结果
     */
    Map<String, Object> getCacheValueMap(CacheManager cacheManager, String cacheName);

    /**
     * 获取缓存结果
     * 
     * @param cacheManager
     *            缓存管理器
     * @param cacheKey
     *            缓存key
     * @return 缓存结果
     */
    Object getCacheValue(CacheManager cacheManager, Object cacheKey);

    /**
     * 驱逐所有缓存
     *
     * @return 驱逐数量
     */
    long evictAll();

    /**
     * 驱逐所有缓存
     *
     * @param cacheName
     *            缓存名称
     * @return 驱逐数量
     */
    long evictAll(String cacheName);

    /**
     * 驱逐所有缓存
     *
     * @param cacheKey
     *            缓存key
     * @return 驱逐数量
     */
    long evictAll(Object cacheKey);

    /**
     * 驱逐缓存
     *
     * @param cacheManager
     *            缓存管理器
     * @return 驱逐数量
     */
    long evict(CacheManager cacheManager);

    /**
     * 驱逐缓存
     *
     * @param cacheManager
     *            缓存管理器
     * @param cacheName
     *            缓存名称
     * @return 驱逐数量
     */
    long evict(CacheManager cacheManager, String cacheName);

    /**
     * 驱逐缓存
     * 
     * @param cacheManager
     *            缓存管理器
     * @param cacheKey
     *            缓存key
     * @return 驱逐数量
     */
    long evict(CacheManager cacheManager, Object cacheKey);

    /**
     * 更新所有缓存
     *
     * @param cacheName
     *            缓存名称
     * @param value
     *            更新值
     * @return 更新数量
     */
    long updateAll(String cacheName, Object value);

    /**
     * 更新所有缓存
     *
     * @param cacheKey
     *            缓存key
     * @param value
     *            更新值
     * @return 更新数量
     */
    long updateAll(Object cacheKey, Object value);

    /**
     * 更新缓存
     *
     * @param cacheManager
     *            缓存管理器
     * @param cacheName
     *            缓存名称
     * @param value
     *            更新值
     * @return 更新数量
     */
    long update(CacheManager cacheManager, String cacheName, Object value);

    /**
     * 更新缓存
     * 
     * @param cacheManager
     *            缓存管理器
     * @param cacheKey
     *            缓存key
     * @param value
     *            更新值
     * @return 更新数量
     */
    long update(CacheManager cacheManager, Object cacheKey, Object value);
}

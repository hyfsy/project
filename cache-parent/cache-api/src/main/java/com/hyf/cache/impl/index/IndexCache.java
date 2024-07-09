package com.hyf.cache.impl.index;

import java.util.Set;

import com.hyf.cache.CacheKey;
import com.hyf.cache.enums.CacheType;

/**
 * 全局索引缓存
 * 
 * @author baB_hyf
 * @date 2022/02/11
 * @see IndexCacheManager
 */
public interface IndexCache
{

    /**
     * 获取缓存名称
     * 
     * @return 缓存名称
     */
    String getCacheName();

    /**
     * 获取缓存的类型
     * 
     * @return 缓存的类型
     */
    CacheType getCacheType();

    /**
     * 获取当前缓存下所有的缓存key
     * 
     * @return 当前缓存下所有的缓存key
     */
    Set<CacheKey> getAllKeys();

    /**
     * 获取当前缓存下pattern匹配的缓存key
     *
     * @param pattern
     *            ant匹配字符串
     * @return 当前缓存下pattern匹配的缓存key
     */
    Set<CacheKey> getAllKeys(String pattern);
}

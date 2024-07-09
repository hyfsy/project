package com.hyf.cache;

import com.hyf.cache.impl.operation.CacheOperationContextBasedCacheResolver;

/**
 * 缓存处理流程配置，提供不同的缓存具体实现
 * 
 * @author baB_hyf
 * @date 2022/02/17
 * @see CacheProcessor
 */
public interface CacheProcessConfigurer
{

    /**
     * 获取缓存管理器具体实现
     * 
     * @return 缓存管理器具体实现
     */
    CacheManager getCacheManager();

    /**
     * 获取缓存解析器具体实现
     * 
     * @return 缓存解析器具体实现
     */
    default CacheResolver getCacheResolver() {
        return new CacheOperationContextBasedCacheResolver(getCacheManager());
    }
}

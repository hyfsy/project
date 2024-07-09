package com.hyf.cache;

import java.util.List;

/**
 * {@link Cache} 对象的解析器，基于缓存操作上下文信息获取到{@link Cache}对象列表
 * 
 * @author baB_hyf
 * @date 2022/01/14
 * @see CacheManager
 * @see CacheOperationContext
 */
public interface CacheResolver extends org.springframework.cache.interceptor.CacheResolver
{

    /**
     * 通过缓存的操作上下文信息获取到{@link Cache}对象列表
     *
     * @param context
     *            缓存操作上下文
     * @return 当前使用的缓存对象列表
     */
    List<Cache> resolveCaches(CacheOperationContext<?> context);

    // TODO 需要做好第一次调用缓存的情况，key为类、方法、参数，标记已调用过
    @Deprecated
    Object resolveCacheValue(CacheOperationContext<?> context, CacheManager cacheManager);
}

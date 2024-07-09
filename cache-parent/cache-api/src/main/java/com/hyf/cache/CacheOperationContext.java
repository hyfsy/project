package com.hyf.cache;

import java.util.Collection;
import java.util.List;

import org.springframework.cache.interceptor.CacheOperationInvocationContext;

/**
 * 缓存操作选项对象，包含注解指定的选项配置
 *
 * @author baB_hyf
 * @date 2022/01/15
 * @see CacheOperationContexts
 * @see BaseCacheOperation
 */
public interface CacheOperationContext<O extends BaseCacheOperation> extends CacheOperationInvocationContext<O>
{

    /**
     * 获取缓存名称列表
     * 
     * @return 缓存名称列表
     */
    Collection<String> getCacheNames();

    /**
     * 获取缓存key
     * 
     * @param cache
     *            缓存对象
     * @return 缓存key列表
     */
    List<Object> getCacheKey(Cache cache);

    /**
     * 获取缓存key
     * 
     * @param cache
     *            缓存对象
     * @param result
     *            方法调用的结果对象
     * @return 缓存key列表
     */
    List<Object> getCacheKey(Cache cache, Object result);
}

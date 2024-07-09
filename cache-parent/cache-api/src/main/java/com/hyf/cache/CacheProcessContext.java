package com.hyf.cache;

import org.springframework.cache.interceptor.CacheOperationInvoker;

import com.hyf.cache.impl.context.CacheAnnotationContext;

/**
 * 缓存处理上下文，贯穿整个缓存操作流程
 *
 * @author baB_hyf
 * @date 2022/02/16
 * @see com.hyf.cache.impl.aspect.EpCacheInterceptor
 * @see CacheProcessor
 */
public interface CacheProcessContext
{

    /**
     * 获取业务方法调用对象
     * 
     * @return 业务方法调用对象
     */
    CacheOperationInvoker getInvoker();

    /**
     * 获取方法调用上下文对象
     *
     * @return 方法调用上下文对象
     */
    InvocationContext getInvocationContext();

    /**
     * 获取缓存操作上下文s对象
     *
     * @return 缓存操作上下文s对象
     */
    CacheOperationContexts getCacheOperationContexts();

    /**
     * 获取缓存注解上下文对象
     *
     * @return 缓存注解上下文对象
     */
    CacheAnnotationContext getCacheAnnotationContext();
}

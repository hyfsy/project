package com.hyf.cache.impl.context;

import java.lang.reflect.Method;

import org.springframework.cache.interceptor.CacheOperationInvoker;

import com.hyf.cache.CacheOperationContexts;
import com.hyf.cache.CacheProcessContext;
import com.hyf.cache.InvocationContext;

/**
 * 缓存处理上下文默认实现
 *
 * @author baB_hyf
 * @date 2022/02/09
 * @see com.hyf.cache.CacheProcessor
 */
public class DefaultCacheProcessContext implements CacheProcessContext
{

    private final CacheOperationInvoker invoker;
    private final InvocationContext invocationContext;
    private final CacheOperationContexts cacheOperationContexts;
    private final CacheAnnotationContext cacheAnnotationContext;

    public DefaultCacheProcessContext(CacheOperationInvoker invoker, Object target, Method method, Object[] args,
            CacheOperationContexts cacheOperationContexts) {

        this.invoker = invoker;
        this.invocationContext = new DefaultInvocationContext(target, method, args);
        this.cacheOperationContexts = cacheOperationContexts;
        this.cacheAnnotationContext = new CacheAnnotationContext(invocationContext.getTargetClass(),
                invocationContext.getTargetMethod());
    }

    @Override
    public CacheOperationInvoker getInvoker() {
        return invoker;
    }

    @Override
    public InvocationContext getInvocationContext() {
        return invocationContext;
    }

    @Override
    public CacheOperationContexts getCacheOperationContexts() {
        return cacheOperationContexts;
    }

    @Override
    public CacheAnnotationContext getCacheAnnotationContext() {
        return cacheAnnotationContext;
    }
}

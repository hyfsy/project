package com.hyf.cache.impl.operation;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Collections;

import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import com.hyf.cache.BaseCacheOperation;
import com.hyf.cache.CacheOperationContext;
import com.hyf.cache.CacheOperationContexts;

/**
 * 缓存操作上下文s对象默认实现
 *
 * @author baB_hyf
 * @date 2022/02/09
 * @see CacheOperationContext
 */
public class DefaultCacheOperationContexts implements CacheOperationContexts
{

    /** opClass -> options */
    private final MultiValueMap<Class<? extends BaseCacheOperation>, CacheOperationContext<BaseCacheOperation>> contexts;

    public DefaultCacheOperationContexts(Collection<? extends BaseCacheOperation> operations, Method method,
            Object[] args, Object target, Class<?> targetClass) {
        this.contexts = new LinkedMultiValueMap<>(operations.size());
        for (BaseCacheOperation op : operations) {
            this.contexts.add(op.getClass(), getOperationContext(op, method, args, target, targetClass));
        }
    }

    @Override
    public Collection<CacheOperationContext<BaseCacheOperation>> get(
            Class<? extends BaseCacheOperation> operationClass) {
        Collection<CacheOperationContext<BaseCacheOperation>> result = this.contexts.get(operationClass);
        return (result != null ? result : Collections.emptyList());
    }

    // TODO 缓存
    protected CacheOperationContext<BaseCacheOperation> getOperationContext(BaseCacheOperation operation, Method method,
            Object[] args, Object target, Class<?> targetClass) {
        return new DefaultCacheOperationContext(operation, target, method, args);
    }
}

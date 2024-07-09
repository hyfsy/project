package com.hyf.cache.impl.operation;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.springframework.context.expression.AnnotatedElementKey;

import com.hyf.cache.BaseCacheOperation;
import com.hyf.cache.Cache;
import com.hyf.cache.CacheOperationContext;
import com.hyf.cache.InvocationContext;
import com.hyf.cache.impl.context.DefaultInvocationContext;
import com.hyf.cache.impl.key.CompositeCacheKey;
import com.hyf.cache.impl.key.generator.KeyGenerateManager;

/**
 * 缓存操作选项对象的默认实现
 *
 * @author baB_hyf
 * @date 2022/02/08
 * @see BaseCacheOperation
 */
public class DefaultCacheOperationContext implements CacheOperationContext<BaseCacheOperation>
{

    private final InvocationContext invocationContext;

    private final BaseCacheOperation operation;

    private final AnnotatedElementKey methodKey;

    private Collection<String> cacheNames;

    public DefaultCacheOperationContext(BaseCacheOperation operation, Object target, Method method, Object[] args) {
        this.operation = operation;

        this.invocationContext = DefaultInvocationContext.simple(target, method, args);

        this.methodKey = new AnnotatedElementKey(this.invocationContext.getTargetMethod(),
                this.invocationContext.getTargetClass());

        this.cacheNames = operation.getCacheNames();
    }

    @Override
    public Collection<String> getCacheNames() {
        return this.cacheNames;
    }

    @Override
    public List<Object> getCacheKey(Cache cache) {
        return getCacheKey(cache, null);
    }

    @Override
    public List<Object> getCacheKey(Cache cache, Object result) {

        Object cacheKey = KeyGenerateManager.generate(cache.getName(), operation, invocationContext);

        if (cacheKey instanceof CompositeCacheKey) {
            CompositeCacheKey compositeCacheKey = (CompositeCacheKey) cacheKey;
            return (List) compositeCacheKey.getCacheKeyList();
        }
        else {
            return Collections.singletonList(cacheKey);
        }
    }

    @Override
    public BaseCacheOperation getOperation() {
        return this.operation;
    }

    @Override
    public Object getTarget() {
        return this.invocationContext.getTarget();
    }

    @Override
    public Method getMethod() {
        return this.invocationContext.getTargetMethod();
    }

    @Override
    public Object[] getArgs() {
        return this.invocationContext.getArgs();
    }
}

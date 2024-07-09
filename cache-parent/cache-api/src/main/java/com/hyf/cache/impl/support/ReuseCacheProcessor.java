package com.hyf.cache.impl.support;

import java.lang.reflect.Method;
import java.lang.reflect.Type;

import org.springframework.cache.support.NullValue;
import org.springframework.cache.support.SimpleValueWrapper;
import org.springframework.expression.EvaluationContext;

import com.hyf.cache.*;
import com.hyf.cache.bus.CacheResultResolver;
import com.hyf.cache.impl.operation.ReuseCacheableOperation;
import com.hyf.cache.impl.operation.ReuseModifiableOperation;
import com.hyf.cache.impl.spel.EvaluationManager;
import com.hyf.cache.impl.spel.SpELCacheResultResolver;
import com.hyf.cache.impl.utils.StringUtils;

/**
 * TODO 可复用缓存的执行逻辑
 * 
 * @author baB_hyf
 * @date 2022/02/17
 * @see com.hyf.cache.impl.operation.ReuseCacheableOperation
 * @see com.hyf.cache.impl.operation.ReuseModifiableOperation
 */
public class ReuseCacheProcessor extends BaseCacheProcessor
{

    @Override
    protected CacheValueWrapper doExecute(CacheProcessContext context, CacheProcessChain chain) throws CacheException {

        // TODO

        return null;
    }

    @Override
    public boolean support(CacheProcessContext context) {
        CacheOperationContexts cacheOperationContexts = context.getCacheOperationContexts();
        return cacheOperationContexts.get(ReuseCacheableOperation.class).size() > 0
                || cacheOperationContexts.get(ReuseModifiableOperation.class).size() > 0;
    }

    @Override
    public void sync(CacheProcessContext context, CacheValueWrapper value, CacheProcessChain chain)
            throws CacheException {

    }

    /**
     * TODO 可复用缓存功能
     *
     * 方法返回值处理，搞成缓存结果
     *
     * @param invocationContext
     *            调用上下文
     * @param operation
     *            缓存操作
     * @param cacheHit
     *            缓存结果
     * @return 方法返回值的包装
     */
    private org.springframework.cache.Cache.ValueWrapper returnResultResolve(InvocationContext invocationContext,
            ReuseCacheableOperation operation, org.springframework.cache.Cache.ValueWrapper cacheHit) {

        CacheResultResolver<Object> cacheResultResolver = null;

        String cacheResultResolverString = operation.getCacheResultResolver();
        if (!StringUtils.isBlank(cacheResultResolverString)) {
            cacheResultResolver = getCacheResultResolver(invocationContext.getMethod(), cacheResultResolverString);
        }
        else if (!StringUtils.isBlank(operation.getCacheProperty())) {
            EvaluationContext evaluationContext = EvaluationManager.createEvaluationContext(invocationContext);
            cacheResultResolver = new SpELCacheResultResolver(evaluationContext, operation.getCacheProperty(),
                    invocationContext.getTargetClass(), invocationContext.getMethod());
        }

        if (cacheResultResolver != null) {
            cacheHit = wrapValue(cacheResultResolver.convertCacheResult(invocationContext.getArgs(), cacheHit.get()));
        }

        return cacheHit;
    }

    private CacheResultResolver<Object> getCacheResultResolver(Method method, String hint) {
        // TODO
        Type genericReturnType = method.getGenericReturnType();
        return null;
    }

    /**
     * 值结果包装为缓存结果对象
     *
     * @param value
     *            值对象
     * @return 缓存结果对象
     */
    private org.springframework.cache.Cache.ValueWrapper wrapValue(Object value) {
        if (value == null || value instanceof NullValue) {
            return new SimpleValueWrapper(null);
        }
        else {
            return new SimpleValueWrapper(value);
        }
    }

    public CacheManager getCacheManager() {
        return getConfigurer().getCacheManager();
    }

    public CacheResolver getCacheResolver() {
        return getConfigurer().getCacheResolver();
    }

}

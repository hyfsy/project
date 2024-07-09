package com.hyf.cache.impl.context;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

import org.springframework.core.annotation.AnnotatedElementUtils;

import com.hyf.cache.annotation.*;
import com.hyf.cache.bus.annotation.ReuseCacheable;
import com.hyf.cache.bus.annotation.ReuseModifiable;

/**
 * TODO 暂定
 *
 * 缓存注解上下文对象
 *
 * @author baB_hyf
 * @date 2022/02/08
 * @see com.hyf.cache.CacheProcessContext
 */
public class CacheAnnotationContext
{

    private final Class<?> targetClass;
    private final Method targetMethod;

    private final EpCacheable cacheable;
    private final EpCacheEvict cacheEvict;
    private final CacheKey cacheKey;
    private final CacheCondition cacheCondition;
    private final CacheTTL cacheTTL;

    private final ReuseCacheable reuseCacheable;
    private final ReuseModifiable reuseModifiable;

    public CacheAnnotationContext(Class<?> targetClass, Method targetMethod) {
        this.targetClass = targetClass;
        this.targetMethod = targetMethod;
        this.cacheable = parseAnnotation(targetClass, targetMethod, EpCacheable.class);
        this.cacheEvict = parseAnnotation(targetClass, targetMethod, EpCacheEvict.class);
        this.cacheKey = parseAnnotation(targetClass, targetMethod, CacheKey.class);
        this.cacheCondition = parseAnnotation(targetClass, targetMethod, CacheCondition.class);
        this.cacheTTL = parseAnnotation(targetClass, targetMethod, CacheTTL.class);
        this.reuseCacheable = parseAnnotation(targetClass, targetMethod, ReuseCacheable.class);
        this.reuseModifiable = parseAnnotation(targetClass, targetMethod, ReuseModifiable.class);
        validate();
    }

    public EpCacheable getCacheable() {
        return cacheable;
    }

    public EpCacheEvict getCacheEvict() {
        return cacheEvict;
    }

    public CacheKey getCacheKey() {
        return cacheKey;
    }

    public CacheCondition getCacheCondition() {
        return cacheCondition;
    }

    public CacheTTL getCacheTTL() {
        return cacheTTL;
    }

    public ReuseCacheable getReuseCacheable() {
        return reuseCacheable;
    }

    public ReuseModifiable getReuseModifiable() {
        return reuseModifiable;
    }

    public Class<?> getTargetClass() {
        return targetClass;
    }

    public Method getTargetMethod() {
        return targetMethod;
    }

    private <A extends Annotation> A parseAnnotation(Class<?> targetClass, Method targetMethod,
            Class<A> annotationType) {
        A mergedAnnotation = AnnotatedElementUtils.findMergedAnnotation(targetMethod, annotationType);
        if (mergedAnnotation == null) {
            mergedAnnotation = AnnotatedElementUtils.findMergedAnnotation(targetClass, annotationType);
        }
        return mergedAnnotation;
    }

    private void validate() {
        if (cacheKey == null) {
            throw new IllegalArgumentException("Method or class has no cache key config.");
        }
    }
}

package com.hyf.cache.impl.annotation;

import java.lang.reflect.AnnotatedElement;

import org.springframework.core.annotation.AnnotatedElementUtils;

import com.hyf.cache.annotation.*;
import com.hyf.cache.bus.annotation.ReuseCacheable;
import com.hyf.cache.bus.annotation.ReuseModifiable;

/**
 * 缓存注解相关工具类
 *
 * @author baB_hyf
 * @date 2022/02/08
 */
public abstract class CacheAnnotationUtils
{

    // public static Cacheable getMergedCacheableAttributes(Class<?> clazz, Method
    // method) {
    // Cacheable classAnnotation = AnnotatedElementUtils.findMergedAnnotation(clazz,
    // Cacheable.class);
    // Cacheable methodAnnotation =
    // AnnotatedElementUtils.findMergedAnnotation(clazz, Cacheable.class);
    // }

    public static EpCacheable getCacheable(AnnotatedElement element) {
        return AnnotatedElementUtils.findMergedAnnotation(element, EpCacheable.class);
    }

    public static EpCacheEvict getCacheEvict(AnnotatedElement element) {
        return AnnotatedElementUtils.findMergedAnnotation(element, EpCacheEvict.class);
    }

    public static ReuseCacheable getReuseCacheable(AnnotatedElement element) {
        return AnnotatedElementUtils.findMergedAnnotation(element, ReuseCacheable.class);
    }

    public static ReuseModifiable getReuseModifiable(AnnotatedElement element) {
        return AnnotatedElementUtils.findMergedAnnotation(element, ReuseModifiable.class);
    }

    public static CacheKey getCacheKey(AnnotatedElement element) {
        return AnnotatedElementUtils.findMergedAnnotation(element, CacheKey.class);
    }

    public static CacheCondition getCacheCondition(AnnotatedElement element) {
        return AnnotatedElementUtils.findMergedAnnotation(element, CacheCondition.class);
    }

    public static CacheTTL getCacheTTL(AnnotatedElement element) {
        return AnnotatedElementUtils.findMergedAnnotation(element, CacheTTL.class);
    }

    // public static <T, A extends Annotation> T
    // getMergedAttributes(AnnotatedElement element, Class<A> annotationType,
    // Class<T> returnType) {
    // A mergedAnnotation = AnnotatedElementUtils.findMergedAnnotation(element,
    // annotationType);
    // if (mergedAnnotation == null) {
    // return null;
    // }
    //
    // return mergedAnnotation;
    // }

    public static EpCacheable mergeCacheable(EpCacheable one, EpCacheable two) {
        if (one == null) {
            return two;
        }
        if (two == null) {
            return one;
        }

        return null;
    }

}

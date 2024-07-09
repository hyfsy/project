package com.hyf.cache.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.core.annotation.AliasFor;

import com.hyf.cache.constants.CacheConstants;
import com.hyf.cache.enums.CacheType;

/**
 * 方法缓存删除注解
 *
 * @author baB_hyf
 * @date 2022/01/14
 */
@Target({ElementType.TYPE, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@CacheKey
@CacheCondition
@CacheTTL
public @interface EpCacheEvict
{

    // String cacheManager() default "";

    /**
     * 指定springBean对象的名称，用来获取查找的{@link com.hyf.cache.Cache}对象
     * <p>
     * 需要实现 {@link com.hyf.cache.CacheResolver} 接口
     */
    String cacheResolver() default "";

    /**
     * 添加缓存时是否使用全局锁保证数据的强一致，兼容原生spring参数
     */
    boolean sync() default CacheConstants.FORCE_CONSISTENCY;

    /**
     * 添加缓存时是否使用全局锁保证数据的强一致
     */
    boolean forceConsistency() default CacheConstants.FORCE_CONSISTENCY;

    /**
     * 哪些缓存类型需要删除，默认全删
     */
    CacheType[] cacheType() default {};

    /**
     * 删除无指定key的，即所有无id的缓存
     */
    boolean allKeysNoSpecified() default true;

    /**
     * 是否删除当前cacheName下所有的缓存
     */
    boolean allEntries() default true;

    /**
     * 是否在方法执行完毕后删除缓存
     */
    boolean beforeInvocation() default false;

    // TODO 指定key的相关属性，支持删除多个key

    // CacheKey

    @AliasFor(annotation = CacheKey.class, attribute = "value")
    String value() default "";

    @AliasFor(annotation = CacheKey.class, attribute = "cacheNames")
    String cacheNames() default "";

    // 必须指定
    @AliasFor(annotation = CacheKey.class, attribute = "cacheClass")
    Class<?> cacheClass() default Class.class;

    @AliasFor(annotation = CacheKey.class, attribute = "cacheClassName")
    String cacheClassName() default "";

    @AliasFor(annotation = CacheKey.class, attribute = "key")
    String key() default "";

    @AliasFor(annotation = CacheKey.class, attribute = "keyGenerator")
    String keyGenerator() default "";

    // CacheCondition

    @AliasFor(annotation = CacheCondition.class, attribute = "condition")
    String condition() default "";

    @AliasFor(annotation = CacheCondition.class, attribute = "unless")
    String unless() default "";

    @AliasFor(annotation = CacheCondition.class, attribute = "conditionMethod")
    String conditionMethod() default "";

    @AliasFor(annotation = CacheCondition.class, attribute = "conditionArgs")
    String conditionArgs() default "#p0";

    @AliasFor(annotation = CacheCondition.class, attribute = "unlessMethod")
    String unlessMethod() default "";

    @AliasFor(annotation = CacheCondition.class, attribute = "unlessArgs")
    String unlessArgs() default "#p0";

    // @AliasFor(annotation = CacheCondition.class, attribute = "allowNull")
    // boolean allowNull() default true;

    // // CacheTTL
    //
    // @AliasFor(annotation = CacheTTL.class, attribute = "ttl")
    // long ttl() default CacheConstants.DEFAULT_TTL;
    //
    // @AliasFor(annotation = CacheTTL.class, attribute = "unit")
    // TimeUnit unit() default TimeUnit.MINUTES;
    //
    // @AliasFor(annotation = CacheTTL.class, attribute = "random")
    // boolean random() default true;
}

package com.hyf.cache.annotation;

import java.lang.annotation.*;
import java.util.concurrent.TimeUnit;

import org.springframework.core.annotation.AliasFor;

import com.hyf.cache.constants.CacheConstants;
import com.hyf.cache.enums.CacheType;
import com.hyf.cache.enums.StoreType;

/**
 * 方法缓存的查询注解，没有缓存会根据需要放入
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
public @interface EpCacheable
{

    // String cacheManager() default "";

    /**
     * 指定springBean对象的名称，用来获取查找的{@link com.hyf.cache.Cache}对象
     * <p>
     * 需要实现 {@link com.hyf.cache.CacheResolver} 接口
     */
    String cacheResolver() default "";

    // CacheMode cacheMode() default CacheMode.CACHE_ASIDE_PATTERN;

    /**
     * 添加缓存时是否使用全局锁保证数据的强一致，兼容原生spring参数
     */
    boolean sync() default CacheConstants.FORCE_CONSISTENCY;

    /**
     * 添加缓存时是否使用全局锁保证数据的强一致
     */
    boolean forceConsistency() default CacheConstants.FORCE_CONSISTENCY;

    /**
     * 指定使用哪些缓存中间件进行缓存
     */
    CacheType[] cacheType() default {};

    /**
     * TODO
     *
     * 缓存存储类型，指定结果在缓存中的实际存储的数据结构
     */
    StoreType storeType() default StoreType.AUTO;

    /**
     * 缓存名不存在时，是否动态创建对应的{@link com.hyf.cache.Cache}对象
     */
    boolean dynamicCreate() default true;

    /**
     * 指定springBean对象的名称，用于在业务方法调用后，对结果进行转换，和在缓存获取后，对缓存进行转换
     * <p>
     * 需要实现 {@link com.hyf.cache.CacheResultConverter} 接口
     */
    String cacheResultConverter() default "";

    // ---------------------------------------------------------------------

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

    @AliasFor(annotation = CacheCondition.class, attribute = "allowNull")
    boolean allowNull() default true;

    // CacheTTL

    @AliasFor(annotation = CacheTTL.class, attribute = "ttl")
    long ttl() default CacheConstants.DEFAULT_TTL;

    @AliasFor(annotation = CacheTTL.class, attribute = "unit")
    TimeUnit unit() default TimeUnit.MINUTES;

    @AliasFor(annotation = CacheTTL.class, attribute = "random")
    boolean random() default true;
}

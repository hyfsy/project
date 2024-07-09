package com.hyf.cache.annotation;

import java.lang.annotation.*;

import org.springframework.core.annotation.AliasFor;

/**
 * 缓存key的生成策略注解
 *
 * @author baB_hyf
 * @date 2022/01/14
 */
@Target({ElementType.TYPE, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface CacheKey
{

    /**
     * 缓存名称数组，区分不同业务的缓存
     */
    @AliasFor("cacheNames")
    String[] value() default "";

    /**
     * 缓存名称数组，区分不同业务的缓存
     */
    @AliasFor("value")
    String[] cacheNames() default "";

    /**
     * 缓存类名称，区分统一业务下的不同类型的缓存
     */
    Class<?> cacheClass() default Class.class;

    /**
     * 缓存类名称，区分统一业务下的不同类型的缓存
     */
    String cacheClassName() default "";

    /**
     * 缓存主键key，作为默认策略中标识的一部分使用
     */
    String key() default "";

    // 生成的key放在 custom 下

    /**
     * 用户自定义标识，完全覆盖默认key生成规则，缓存也需要自行维护
     */
    String keyGenerator() default "";

}

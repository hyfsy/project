package com.hyf.cache.annotation;

import java.lang.annotation.*;
import java.util.concurrent.TimeUnit;

import org.springframework.core.annotation.AliasFor;

/**
 * 缓存失效时间的控制注解
 *
 * @author baB_hyf
 * @date 2022/01/14
 */
@Target({ElementType.TYPE, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface CacheTTL
{

    /**
     * 指定ttl的数量，配合{@link #unit()}使用
     */
    @AliasFor("ttl")
    long value() default 30;

    /**
     * 指定ttl的数量，配合{@link #unit()}使用
     */
    @AliasFor("value")
    long ttl() default 30;

    /**
     * {@link #ttl()}的单位
     */
    TimeUnit unit() default TimeUnit.MINUTES;

    /**
     * 失效时间是否添加随机增量，批量缓存时防止缓存雪崩问题
     */
    boolean random() default true;

    // CacheType[] cacheType() default {};
}

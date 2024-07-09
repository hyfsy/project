package com.hyf.cache.annotation;

import java.lang.annotation.*;

/**
 * 是否进行缓存的条件判断注解
 *
 * @author baB_hyf
 * @date 2022/01/16
 */
@Target({ElementType.TYPE, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface CacheCondition
{

    /**
     * spel表达式，true则执行缓存操作，业务方法前判断
     * <p>
     * 优先于{@link #conditionMethod()}
     */
    String condition() default "";

    /**
     * spel表达式，true则执行缓存操作，业务方法后判断，可额外获取result参数
     * <p>
     * 优先于{@link #unlessMethod()}
     */
    String unless() default "";

    /**
     * 当前注解所在类的方法名 or 全类名+方法名（静态方法）
     * <p>
     * 方法需要返回boolean值指定是否执行缓存操作，方法参数为{@link #conditionArgs()}指定的参数
     * <p>
     * 同时存在{@link #condition()}则会被忽略
     */
    String conditionMethod() default "";

    /**
     * spel指定{@link #conditionMethod()}方法的参数
     */
    String conditionArgs() default "#p0";

    /**
     * 当前注解所在类的方法名 or 全类名+方法名（静态方法）
     * <p>
     * 方法需要返回boolean值指定是否执行缓存操作，方法参数为{@link #unlessArgs()}指定的参数
     * <p>
     * 同时存在{@link #condition()}则会被忽略
     */
    String unlessMethod() default "";

    /**
     * spel指定{@link #unlessMethod()}方法的参数
     */
    String unlessArgs() default "#p0";

    /**
     * 返回true表示指定当方法返回值为null时，允许应用缓存存储，缓存驱逐时无效
     */
    boolean allowNull() default true;
}

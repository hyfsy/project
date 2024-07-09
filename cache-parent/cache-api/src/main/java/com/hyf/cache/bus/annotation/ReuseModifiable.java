package com.hyf.cache.bus.annotation;

import java.lang.annotation.*;

import org.springframework.core.annotation.AliasFor;

import com.hyf.cache.annotation.CacheKey;
import com.hyf.cache.constants.CacheConstants;
import com.hyf.cache.enums.CacheType;
import com.hyf.cache.enums.ModifyOperation;

/**
 * 可复用实体的缓存修改注解
 *
 * @author baB_hyf
 * @date 2022/01/18
 */
@Target({ElementType.TYPE, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@CacheKey
public @interface ReuseModifiable
{

    /**
     * 缓存操作类型：新增、修改、删除
     */
    ModifyOperation op();

    /**
     * 映射的索引，多个表示组合索引
     */
    String[] mapperIdx();

    /**
     * 指定springBean对象的名称，复杂修改操作需要进行自定义处理
     * <p>
     * 需要实现 {@link com.hyf.cache.bus.ReuseCacheModifier} 接口
     */
    String reuseCacheModifier() default "";

    /**
     * 指定springBean对象的名称，用来获取查找的{@link com.hyf.cache.Cache}对象
     * <p>
     * 需要实现 {@link com.hyf.cache.CacheResolver} 接口
     */
    String cacheResolver() default "";

    /**
     * 添加缓存时是否使用全局锁保证数据的强一致
     */
    boolean forceConsistency() default CacheConstants.FORCE_CONSISTENCY;

    /**
     * 指定使用哪些缓存中间件进行缓存
     */
    CacheType[] cacheType() default {};

    /**
     * 是否在方法执行完毕后删除缓存
     */
    boolean beforeInvocation() default false;

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
}

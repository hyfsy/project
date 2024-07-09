package com.hyf.cache.bus.annotation;

import java.lang.annotation.*;

import org.springframework.core.annotation.AliasFor;

import com.hyf.cache.annotation.CacheKey;
import com.hyf.cache.constants.CacheConstants;
import com.hyf.cache.enums.CacheType;
import com.hyf.cache.enums.StoreType;

/**
 * 可复用实体的缓存注解
 *
 * @author baB_hyf
 * @date 2022/01/17
 */
@Target({ElementType.TYPE, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@CacheKey
public @interface ReuseCacheable
{

    /**
     * 缓存操作类型：新增、修改、删除
     */
    String[] mapperIdx();

    /**
     * spel表达式，缓存返回值的映射属性
     */
    String cacheProperty() default "";

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
     * 指定springBean对象的名称，用于在业务方法调用后，对结果进行转换
     * <p>
     * 方法参数为 [方法返回结果对象类型，业务方法所有参数]，返回类型为{@link Object}
     * <p>
     * 需要实现 {@link com.hyf.cache.bus.CacheResultResolver} 接口
     */
    String cacheResultResolver() default "";

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

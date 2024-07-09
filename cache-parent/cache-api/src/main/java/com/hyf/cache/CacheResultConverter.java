package com.hyf.cache;

/**
 * 自定义缓存值的获取方式
 *
 * @author baB_hyf
 * @date 2022/01/18
 */
public interface CacheResultConverter<R>
{

    /**
     * 将方法返回值转换为缓存结果
     *
     * @param params
     *            方法参数
     * @param returnValue
     *            方法返回值
     * @return 缓存结果
     */
    Object convertReturnResult(Object[] params, R returnValue);

    /**
     * 将缓存结果转换为方法返回值
     *
     * @param params
     *            方法参数
     * @param cacheValue
     *            缓存结果
     * @return 方法返回值
     */
    R convertCacheResult(Object[] params, Object cacheValue);
}

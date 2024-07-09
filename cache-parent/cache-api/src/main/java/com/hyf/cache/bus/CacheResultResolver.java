package com.hyf.cache.bus;

/**
 * 缓存结果转换器，用于可复用缓存获取缓存结果后，进行缓存操作
 *
 * @author baB_hyf
 * @date 2022/02/11
 */
public interface CacheResultResolver<R>
{

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

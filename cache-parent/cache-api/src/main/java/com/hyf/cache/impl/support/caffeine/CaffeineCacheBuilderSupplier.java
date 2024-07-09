package com.hyf.cache.impl.support.caffeine;

import com.github.benmanes.caffeine.cache.Caffeine;

/**
 * caffeine缓存构建器的提供器
 * 
 * @author baB_hyf
 * @date 2022/02/17
 * @see Caffeine#newBuilder()
 */
public interface CaffeineCacheBuilderSupplier
{

    /**
     * 获取caffeine缓存构建器
     * 
     * @param cacheName
     *            缓存名称
     * @return caffeine缓存构建器
     */
    Caffeine<Object, Object> get(String cacheName);
}

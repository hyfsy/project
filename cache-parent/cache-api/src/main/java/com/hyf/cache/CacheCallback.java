package com.hyf.cache;

/**
 * 基于代码的缓存功能
 * 
 * @author baB_hyf
 * @date 2022/01/14
 * @see com.hyf.cache.impl.CacheTemplate
 */
public interface CacheCallback<T>
{
    T doInCache();
}

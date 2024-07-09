package com.hyf.cache;

/**
 * 基于代码的缓存功能，无需返回值
 * 
 * @author baB_hyf
 * @date 2022/01/16
 * @see com.hyf.cache.impl.CacheTemplate
 */
public abstract class CacheCallbackWithoutResult implements CacheCallback<Object>
{

    @Override
    public Object doInCache() {
        doInCacheWithoutResult();
        return null;
    }

    protected abstract void doInCacheWithoutResult();
}

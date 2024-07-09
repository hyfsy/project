package com.hyf.cache.impl;

import com.hyf.cache.CacheCallback;
import com.hyf.cache.CacheException;

/**
 * 基于代码的缓存功能
 * 
 * @author baB_hyf
 * @date 2022/01/14
 * @see CacheDefinition
 */
public class CacheTemplate
{

    public static final CacheDefinition DEFAULT_DEFINITION = new CacheDefinition();

    private final CacheDefinition rootCacheDefinition;

    public CacheTemplate() {
        this.rootCacheDefinition = DEFAULT_DEFINITION;
    }

    public CacheTemplate(CacheDefinition root) {
        this.rootCacheDefinition = root;
    }

    public <T> T execute(CacheCallback<T> action) throws CacheException {
        return execute(action, null);
    }

    public <T> T executeEvict(CacheCallback<T> action) throws CacheException {
        return executeEvict(action, null);
    }

    public <T> T execute(CacheCallback<T> action, CacheDefinition combined) throws CacheException {
        // TODO
        return action.doInCache();
    }

    public <T> T executeEvict(CacheCallback<T> action, CacheDefinition combined) throws CacheException {
        // TODO
        return action.doInCache();
    }
}

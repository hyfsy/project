package com.hyf.cache;

import java.util.Collection;

/**
 * 缓存操作上下文s对象
 * 
 * @author baB_hyf
 * @date 2022/02/16
 * @see CacheProcessContext
 */
public interface CacheOperationContexts
{

    /**
     * 通过缓存选项操作类获取对应的缓存操作选项对象集合
     * 
     * @param operationClass
     *            缓存选项操作类
     * @return 缓存操作选项对象集合
     */
    Collection<CacheOperationContext<BaseCacheOperation>> get(Class<? extends BaseCacheOperation> operationClass);
}

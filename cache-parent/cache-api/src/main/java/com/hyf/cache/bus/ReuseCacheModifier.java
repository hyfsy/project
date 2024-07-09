package com.hyf.cache.bus;

import com.hyf.cache.BaseCacheOperation;
import com.hyf.cache.Cache;
import com.hyf.cache.CacheOperationContext;

/**
 * 针对复杂操作，用户来自定义可复用缓存的更新处理
 *
 * @author baB_hyf
 * @date 2022/01/20
 */
public interface ReuseCacheModifier<O extends BaseCacheOperation>
{

    /**
     * 自定义缓存修改
     * 
     * @param cacheProcessContext
     *            缓存处理上下文
     * @param cache
     *            当前缓存对象
     */
    void modify(CacheOperationContext<O> cacheProcessContext, Cache cache);
}

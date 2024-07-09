package com.hyf.cache.impl.cache;

import com.hyf.cache.AbstractCacheDecorator;
import com.hyf.cache.Cache;

/**
 * TODO 缓存全局索引处理
 * 
 * @author baB_hyf
 * @date 2022/02/17
 */
public class IndexCache extends AbstractCacheDecorator
{

    public IndexCache(Cache cache) {
        super(cache);
    }

}

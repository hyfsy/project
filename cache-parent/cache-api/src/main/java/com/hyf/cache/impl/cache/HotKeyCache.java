package com.hyf.cache.impl.cache;

import com.hyf.cache.AbstractCacheDecorator;
import com.hyf.cache.Cache;

/**
 * TODO 针对redis的热点key分割
 * 
 * @author baB_hyf
 * @date 2022/02/17
 */
public class HotKeyCache extends AbstractCacheDecorator
{

    public HotKeyCache(Cache cache) {
        super(cache);
    }
}

package com.hyf.cache.impl.mode;

import java.lang.reflect.Method;

import com.hyf.cache.AbstractCacheProviderModeProcessor;
import com.hyf.cache.CacheException;
import com.hyf.cache.CacheProcessContext;
import com.hyf.cache.enums.CacheMode;

/**
 * write behind缓存模式
 *
 * @author baB_hyf
 * @date 2022/02/08
 */
public class WriteBehindModeProcessor extends AbstractCacheProviderModeProcessor
{

    @Override
    public boolean supportCacheMode(CacheMode cacheMode) {
        return CacheMode.WRITE_BEHIND.equals(cacheMode);
    }

    @Override
    public Object execute(Object target, Method method, Object[] args, CacheProcessContext context)
            throws CacheException {
        throw new UnsupportedOperationException("Current not support write behind mode.");
    }
}

package com.hyf.cache.impl.mode;

import java.lang.reflect.Method;

import org.springframework.beans.factory.annotation.Autowired;

import com.hyf.cache.CacheException;
import com.hyf.cache.CacheModeProcessor;
import com.hyf.cache.CacheProcessContext;
import com.hyf.cache.enums.CacheMode;

/**
 * 自定义缓存模式
 *
 * @author baB_hyf
 * @date 2022/02/10
 */
public class CustomCacheModeProcessor implements CacheModeProcessor
{

    @Autowired(required = false)
    private CustomModeProcessor customModeProcessor;

    @Override
    public boolean supportCacheMode(CacheMode cacheMode) {
        return CacheMode.CUSTOM.equals(cacheMode);
    }

    @Override
    public Object execute(Object target, Method method, Object[] args, CacheProcessContext context)
            throws CacheException {
        if (customModeProcessor == null) {
            throw new IllegalArgumentException("CustomModeProcessor instance not exist");
        }
        return customModeProcessor.execute(target, method, args, context);
    }
}

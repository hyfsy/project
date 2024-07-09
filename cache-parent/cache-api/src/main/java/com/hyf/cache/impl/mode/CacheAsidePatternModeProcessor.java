package com.hyf.cache.impl.mode;

import java.lang.reflect.Method;

import com.hyf.cache.*;
import com.hyf.cache.enums.CacheMode;
import com.hyf.cache.impl.utils.ApplicationContextUtils;

/**
 * cache aside pattern缓存模式
 * 
 * @author baB_hyf
 * @date 2022/02/08
 */
public class CacheAsidePatternModeProcessor implements CacheModeProcessor
{

    @Override
    public boolean supportCacheMode(CacheMode cacheMode) {
        return CacheMode.CACHE_ASIDE_PATTERN.equals(cacheMode);
    }

    @Override
    public Object execute(Object target, Method method, Object[] args, CacheProcessContext context)
            throws CacheException {
        // TODO
        CacheValueWrapper result = ApplicationContextUtils.getApplicationContext().getBean(CacheProcessChain.class)
                .execute(context);
        return result.get();
    }
}

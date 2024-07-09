package com.hyf.cache.custom;

import org.springframework.stereotype.Component;

import com.hyf.cache.CacheException;
import com.hyf.cache.CachePostProcessor;
import com.hyf.cache.CacheProcessContext;
import com.hyf.cache.CacheValueWrapper;
import com.hyf.cache.enums.CacheType;

/**
 * @author baB_hyf
 * @date 2022/02/17
 */
@Component
public class TestCachePostProcessor implements CachePostProcessor
{

    @Override
    public boolean postProcessBeforeCacheOperation(CacheType cacheType, CacheProcessContext context) throws CacheException {
        System.out.println("自定义缓存操作前postProcessBeforeCacheOperation 缓存类型: " + cacheType + ";缓存形式（可复用缓存/方法缓存）：" + context.getCacheAnnotationContext());
        return true;
    }

    @Override
    public CacheValueWrapper postProcessAfterCacheOperation(CacheType cacheType, CacheProcessContext context,
            CacheValueWrapper value) throws CacheException {
        System.out.println("postProcessAfterCacheOperation cacheType: " + cacheType);
        return value;
    }

    @Override
    public boolean postProcessBeforeInvokeOperation(CacheProcessContext context) throws Exception {
        System.out.println("自定义缓存操作反射方法前postProcessBeforeInvokeOperation");
        return true;
    }

    @Override
    public CacheValueWrapper postProcessAfterInvokeOperation(CacheProcessContext context, CacheValueWrapper value)
            throws Exception {
        System.out.println("postProcessAfterInvokeOperation value: " + value.get());
        return value;
    }
}

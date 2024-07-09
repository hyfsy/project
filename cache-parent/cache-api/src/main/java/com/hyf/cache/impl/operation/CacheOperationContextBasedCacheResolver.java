package com.hyf.cache.impl.operation;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.springframework.cache.interceptor.CacheOperationInvocationContext;
import org.springframework.cache.interceptor.SimpleCacheResolver;

import com.hyf.cache.*;

/**
 * 基于缓存注解operation的缓存解析器
 *
 * @author baB_hyf
 * @date 2022/02/10
 * @see CacheOperationContext
 */
public class CacheOperationContextBasedCacheResolver extends SimpleCacheResolver implements CacheResolver
{

    private CacheManager cacheManager;

    public CacheOperationContextBasedCacheResolver(CacheManager cacheManager) {
        super(new SpringCacheManagerAdapter(cacheManager));
        this.cacheManager = cacheManager;
    }

    @Override
    public List<Cache> resolveCaches(CacheOperationContext<?> context) {
        Collection<String> cacheNames = context.getCacheNames();
        if (cacheNames == null) {
            return Collections.emptyList();
        }

        List<Cache> result = new ArrayList<>(cacheNames.size());
        for (String cacheName : cacheNames) {
            Cache cache = cacheManager.findCache(cacheName);
            if (cache == null) {
                throw new IllegalArgumentException(
                        "Cannot find cache named '" + cacheName + "' for " + context.getOperation());
            }
            result.add(cache);
        }
        return result;
    }

    @Override
    public Object resolveCacheValue(CacheOperationContext<?> context, CacheManager cacheManager) {
        return null;
    }

    @Override
    public Collection<? extends org.springframework.cache.Cache> resolveCaches(
            CacheOperationInvocationContext<?> context) {
        if (context instanceof CacheOperationContext) {
            return this.resolveCaches((CacheOperationContext<?>) context);
        }
        else {
            return super.resolveCaches(context);
        }
    }
}

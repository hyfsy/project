package com.hyf.cache.impl.support;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import com.hyf.cache.BaseCacheOperation;
import com.hyf.cache.Cache;
import com.hyf.cache.CacheAdapter;
import com.hyf.cache.CacheKey;
import com.hyf.cache.CacheManager;
import com.hyf.cache.CacheOperationContext;
import com.hyf.cache.CacheProcessConfigurer;
import com.hyf.cache.InvocationContext;
import com.hyf.cache.enums.CacheType;
import com.hyf.cache.impl.index.IndexCache;
import com.hyf.cache.impl.index.IndexCacheManager;
import com.hyf.cache.impl.key.generator.CacheKeyBuilder2;
import com.hyf.cache.impl.operation.CacheEvictOperation;
import com.hyf.cache.impl.operation.CacheOperationContextBasedCacheResolver;
import com.hyf.cache.impl.utils.ApplicationContextUtils;
import com.hyf.cache.impl.utils.StringUtils;

/**
 * TODO 代码太多，功能先放在这
 *
 * 方法缓存驱逐功能实现
 *
 * @author baB_hyf
 * @date 2022/02/15
 * @see MethodCacheProcessor
 */
public class MethodCacheEvictor
{

    private CacheManager cacheManager;

    public MethodCacheEvictor(CacheProcessConfigurer configurer) {
        this.cacheManager = configurer.getCacheManager();
    }

    public void evict(InvocationContext invocationContext, Collection<CacheOperationContext<BaseCacheOperation>> cacheEvictOperationContexts, boolean isBeforeInvocation) {

        // 遍历所有注解操作
        for (CacheOperationContext<BaseCacheOperation> cacheOperationContext : cacheEvictOperationContexts) {
            CacheEvictOperation operation = (CacheEvictOperation) cacheOperationContext.getOperation();

            // 过滤非当前执行时机的
            if (isBeforeInvocation && !operation.isBeforeInvocation() || !isBeforeInvocation && operation.isBeforeInvocation()) {
                continue;
            }

            // 校验是否要走缓存
            if (!doCacheOps(operation.getCacheType())) {
                continue;
            }

            // 获取当前操作管理的所有缓存
            Collection<Cache> caches = resolveCaches(cacheOperationContext, operation);

            // 驱逐所有缓存
            if (operation.isAllEntries()) {
                for (Cache cache : caches) {
                    // 获取当前操作对应的所有缓存key
                    List<Object> cacheKeys = cacheOperationContext.getCacheKey(cache, invocationContext.getResult());
                    for (Object key : cacheKeys) {
                        cache.evict(key);
                    }
                }
            }
            else {
                for (Cache cache : caches) {
                    // 获取当前操作对应的所有缓存key
                    List<Object> cacheKeys = cacheOperationContext.getCacheKey(cache, invocationContext.getResult());
                    for (Object key : cacheKeys) {
                        cache.evict(key);
                    }

                    // 还需要删除所有无id的方法缓存key
                    if (operation.isAllKeysNoSpecified()) {
                        // TODO
                        CacheKey k = CacheKeyBuilder2.business(cache.getName()).cacheClassName(operation.getCacheClassName()).method().signature("*").build();
                        // 获取当前缓存的索引缓存
                        IndexCache indexCache = IndexCacheManager.getIndexCache(getCacheManager().getCacheType(), cache.getName());
                        // 获取key对应的所有方法缓存key
                        Set<CacheKey> allKeys = indexCache.getAllKeys(k.toString());
                        allKeys.forEach(cache::evict);
                    }
                }
            }
        }
    }

    private boolean doCacheOps(CacheType[] cacheTypes) {
        boolean doCacheOps = false;
        if (cacheTypes.length == 0) {
            doCacheOps = true;
        }
        else {
            for (CacheType type : cacheTypes) {
                if (type.equals(getCacheManager().getCacheType())) {
                    doCacheOps = true;
                }
            }
        }
        return doCacheOps;
    }

    private Collection<Cache> resolveCaches(CacheOperationContext<BaseCacheOperation> cacheOperationContext, BaseCacheOperation operation) {
        List<Cache> caches = new ArrayList<>();
        String cacheResolver = operation.getCacheResolver();

        if (!StringUtils.isBlank(cacheResolver)) {
            org.springframework.cache.interceptor.CacheResolver resolver = ApplicationContextUtils.getBeanIfExist(cacheResolver, org.springframework.cache.interceptor.CacheResolver.class);
            if (resolver != null) {
                caches.addAll(adaptCaches(resolver.resolveCaches(cacheOperationContext)));
            }
            else {
                CacheOperationContextBasedCacheResolver contextBasedCacheResolver = new CacheOperationContextBasedCacheResolver(getCacheManager());
                caches.addAll(contextBasedCacheResolver.resolveCaches(cacheOperationContext));
            }
        }
        else {
            CacheOperationContextBasedCacheResolver contextBasedCacheResolver = new CacheOperationContextBasedCacheResolver(getCacheManager());
            caches.addAll(contextBasedCacheResolver.resolveCaches(cacheOperationContext));
        }
        return caches;
    }

    private Collection<? extends Cache> adaptCaches(Collection<? extends org.springframework.cache.Cache> caches) {
        List<Cache> adaptedCacheList = new ArrayList<>(caches.size());

        // TODO bug
        for (org.springframework.cache.Cache cache : caches) {
            if (cache instanceof CacheAdapter) {
                adaptedCacheList.add((CacheAdapter) cache);
            }
            else {
                adaptedCacheList.add(CacheAdapter.adapt(cache));
            }
        }

        return adaptedCacheList;
    }

    public CacheManager getCacheManager() {
        return cacheManager;
    }
}

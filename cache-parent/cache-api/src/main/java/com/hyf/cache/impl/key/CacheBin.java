package com.hyf.cache.impl.key;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.hyf.cache.BaseCacheOperation;
import com.hyf.cache.Cache;
import com.hyf.cache.CacheOperationContext;

/**
 * 包装缓存key相关引用，方便操作
 * 
 * @author baB_hyf
 * @date 2022/02/16
 */
public class CacheBin
{

    private CacheOperationContext<BaseCacheOperation> cacheOperationContext;

    private BaseCacheOperation operation;

    private List<CacheKeyBin> cacheKeyBins = new ArrayList<>();

    public CacheOperationContext<BaseCacheOperation> getCacheOperationContext() {
        return cacheOperationContext;
    }

    public void setCacheOperationContext(CacheOperationContext<BaseCacheOperation> cacheOperationContext) {
        this.cacheOperationContext = cacheOperationContext;
    }

    public BaseCacheOperation getOperation() {
        return operation;
    }

    public void setOperation(BaseCacheOperation operation) {
        this.operation = operation;
    }

    public List<CacheKeyBin> getCacheKeyBins() {
        return cacheKeyBins;
    }

    public void setCacheKeyBins(List<CacheKeyBin> cacheKeyBins) {
        this.cacheKeyBins = cacheKeyBins;
    }

    public void addCacheKeyBin(CacheKeyBin cacheKeyBin) {
        this.cacheKeyBins.add(cacheKeyBin);
    }

    public List<Cache> getCaches() {
        return cacheKeyBins.stream().map(CacheKeyBin::getCache).collect(Collectors.toList());
    }

    public Map<Cache, List<Object>> getOrigin() {
        Map<Cache, List<Object>> opCacheKeyMap = new HashMap<>();
        for (CacheKeyBin cacheKeyBin : cacheKeyBins) {
            opCacheKeyMap.put(cacheKeyBin.getCache(), cacheKeyBin.getCacheKeys());
        }
        return opCacheKeyMap;
    }

    public static class CacheKeyBin
    {

        private Cache cache;

        private List<Object> cacheKeys;

        public Cache getCache() {
            return cache;
        }

        public void setCache(Cache cache) {
            this.cache = cache;
        }

        public List<Object> getCacheKeys() {
            return cacheKeys;
        }

        public void setCacheKeys(List<Object> cacheKeys) {
            this.cacheKeys = cacheKeys;
        }
    }
}

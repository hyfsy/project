package com.hyf.cache.impl.key;

import java.util.ArrayList;
import java.util.List;

/**
 * 包装多个缓存key
 * 
 * @author baB_hyf
 * @date 2022/02/09
 */
public class CompositeCacheKey
{

    private final List<AbstractCacheKey> cacheKeyList;

    public CompositeCacheKey() {
        cacheKeyList = new ArrayList<>();
    }

    public CompositeCacheKey(List<AbstractCacheKey> cacheKey) {
        this.cacheKeyList = new ArrayList<>(cacheKey);
    }

    public List<AbstractCacheKey> getCacheKeyList() {
        return cacheKeyList;
    }

    public void add(AbstractCacheKey cacheKey) {
        cacheKeyList.add(cacheKey);
    }
}

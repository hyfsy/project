package com.hyf.cache.impl.index.bus;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import com.hyf.cache.Cache;

/**
 * 可复用缓存索引默认实现
 * 
 * @author baB_hyf
 * @date 2022/02/09
 */
public class ReuseIndexCacheImpl implements ReuseIndexCache
{

    private Cache cache;
    private Map<String, Set<String>> indexMap = new ConcurrentHashMap<>();
    private Map<String, Set<String>> compositeIndexMap = new ConcurrentHashMap<>();

    // for test
    @Deprecated
    public ReuseIndexCacheImpl() {

    }

    public ReuseIndexCacheImpl(Cache cache) {
        this.cache = cache;
    }

    @Override
    public Set<String> getIndex(String fieldName, String fieldValue) {

        return indexMap.get(fieldName + "." + fieldValue);
    }

    @Override
    public Set<String> getCompositeIndex(String[] fieldName, String[] fieldValue) {
        return indexMap.get(String.join("_", fieldName) + "." + String.join("_", fieldValue));
    }

}

package com.hyf.cache.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.hyf.cache.CacheManager;
import com.hyf.cache.CacheMonitor;
import com.hyf.cache.enums.CacheType;
import com.hyf.cache.impl.support.caffeine.AdaptiveCaffeineCacheManager;
import com.hyf.cache.impl.utils.ApplicationContextUtils;

public class CacheMonitorImpl implements CacheMonitor
{

    @Override
    public Map<CacheType, List<String>> getAllCacheName() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<CacheManager> getAllCacheManager() {
        // TODO 这边需要获取所有的启用的CacheManager
        AdaptiveCaffeineCacheManager adaptiveCaffeineCacheManager = ApplicationContextUtils.getApplicationContext().getBean(AdaptiveCaffeineCacheManager.class);
        List<CacheManager> list = new ArrayList<CacheManager>();
        list.add(adaptiveCaffeineCacheManager);
        return list;
    }

    @Override
    public Map<CacheType, Map<String, Map<String, Object>>> getAllCacheValue() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<String> getCacheNameList(CacheType cacheType) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public CacheManager getCacheManager(CacheType cacheType) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Map<String, Map<String, Object>> getCacheValueMap(CacheManager cacheManager) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Map<String, Object> getCacheValueMap(CacheManager cacheManager, String cacheName) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Object getCacheValue(CacheManager cacheManager, Object cacheKey) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public long evictAll() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public long evictAll(String cacheName) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public long evictAll(Object cacheKey) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public long evict(CacheManager cacheManager) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public long evict(CacheManager cacheManager, String cacheName) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public long evict(CacheManager cacheManager, Object cacheKey) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public long updateAll(String cacheName, Object value) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public long updateAll(Object cacheKey, Object value) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public long update(CacheManager cacheManager, String cacheName, Object value) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public long update(CacheManager cacheManager, Object cacheKey, Object value) {
        // TODO Auto-generated method stub
        return 0;
    }

}

package com.hyf.cache.impl.key.lock;

import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;

import com.hyf.cache.Cache;

/**
 * 缓存key全局锁相关的工具类
 * 
 * @author baB_hyf
 * @date 2022/02/16
 */
public class CacheKeyLockUtils
{

    public static Lock getLock(Map<Cache, List<Object>> allCacheKeyMap) {
        return new CacheKeyLock(allCacheKeyMap);
    }

    public static ReadWriteLock getReadWriteLock(Map<Cache, List<Object>> allCacheKeyMap) {
        return new CacheKeyReadWriteLock(allCacheKeyMap);
    }
}

package com.hyf.cache.impl.utils;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * 全局锁工具
 * 
 * @author baB_hyf
 * @date 2022/02/11
 */
public class LockUtils
{

    private static final Map<String, Map<Object, Lock>> jvmLockMap = new ConcurrentHashMap<>(4);
    private static final Map<String, Map<Object, ReadWriteLock>> jvmReadWriteLockMap = new ConcurrentHashMap<>(4);

    public static Lock getJvmLock(String cacheType, Object lockName) {
        return jvmLockMap.computeIfAbsent(cacheType, (m) -> new ConcurrentHashMap<>(256)).computeIfAbsent(lockName,
                (n) -> new ReentrantLock());
    }

    public static ReadWriteLock getJvmReadWriteLock(String cacheType, Object lockName) {
        return jvmReadWriteLockMap.computeIfAbsent(cacheType, (m) -> new ConcurrentHashMap<>(256))
                .computeIfAbsent(lockName, (n) -> new ReentrantReadWriteLock());
    }

    public static Lock getRedisLock(Object name) {
        return RedissonLockUtils.getRedissonLock(name);
    }

    public static ReadWriteLock getRedisReadWriteLock(Object name) {
        return RedissonLockUtils.getRedissonReadWriteLock(name);
    }
}

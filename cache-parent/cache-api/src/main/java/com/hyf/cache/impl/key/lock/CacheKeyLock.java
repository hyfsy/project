package com.hyf.cache.impl.key.lock;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

import com.hyf.cache.Cache;
import com.hyf.cache.Lockable;

/**
 * 缓存全局锁
 * <p>
 * 整合所有缓存key对应的全局锁，保证顺序一致
 *
 * @author baB_hyf
 * @date 2022/02/16
 */
public class CacheKeyLock implements Lock
{

    private final Lock lock;

    /**
     * 构建缓存全局锁
     * 
     * @param cacheKeyMap
     *            cache -> list&lt;cacheKey&gt;
     */
    public CacheKeyLock(Map<Cache, List<Object>> cacheKeyMap) {
        List<Lock> locks = new ArrayList<>();

        // 校验
        cacheKeyMap.values().stream().flatMap(Collection::stream).filter(k -> !(k instanceof Lockable)).findAny()
                .ifPresent(k -> {
                    throw new IllegalArgumentException("Cache key must implement Lockable interface: " + k.getClass());
                });

        // 排序
        cacheKeyMap.keySet().stream().sorted(Comparator.comparing(Cache::getName)).forEach(c -> cacheKeyMap.get(c)
                .stream().sorted().map(Lockable.class::cast).map(k -> c.getLock(k.getLockName())).forEach(locks::add));

        lock = new CompositeLock(locks);
    }

    @Override
    public void lock() {
        lock.lock();
    }

    @Override
    public void lockInterruptibly() throws InterruptedException {
        lock.lockInterruptibly();
    }

    @Override
    public boolean tryLock() {
        return lock.tryLock();
    }

    @Override
    public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
        return lock.tryLock(time, unit);
    }

    @Override
    public void unlock() {
        lock.unlock();
    }

    @Override
    public Condition newCondition() {
        return lock.newCondition();
    }
}

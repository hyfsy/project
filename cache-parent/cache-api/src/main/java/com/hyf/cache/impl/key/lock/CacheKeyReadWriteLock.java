package com.hyf.cache.impl.key.lock;

import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;

import com.hyf.cache.Cache;
import com.hyf.cache.Lockable;

/**
 * 缓存全局读写锁
 * <p>
 * 整合所有缓存key对应的全局读写锁，保证顺序一致
 *
 * @author baB_hyf
 * @date 2022/02/16
 */
public class CacheKeyReadWriteLock implements ReadWriteLock
{

    private final Lock readLock;
    private final Lock writeLock;

    public CacheKeyReadWriteLock(Map<Cache, List<Object>> cacheKeyMap) {

        // 校验
        cacheKeyMap.values().stream().flatMap(Collection::stream).filter(k -> !(k instanceof Lockable)).findAny()
                .ifPresent(k -> {
                    throw new IllegalArgumentException("Cache key must implement Lockable interface: " + k.getClass());
                });

        List<Lock> rs = new ArrayList<>();
        List<Lock> ws = new ArrayList<>();

        // 排序
        cacheKeyMap.keySet().stream().sorted(Comparator.comparing(Cache::getName))
                .forEach(c -> cacheKeyMap.get(c).stream().sorted().map(Lockable.class::cast)
                        .map(k -> c.getReadWriteLock(k.getLockName())).forEach(ls -> {
                            rs.add(ls.readLock());
                            ws.add(ls.writeLock());
                        }));
        readLock = new CompositeLock(rs);
        writeLock = new CompositeLock(ws);
    }

    @Override
    public Lock readLock() {
        return readLock;
    }

    @Override
    public Lock writeLock() {
        return writeLock;
    }
}

package com.hyf.cache;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;

import com.hyf.cache.enums.CacheType;

/**
 * 缓存对象，执行实际的缓存操作
 *
 * @author baB_hyf
 * @date 2022/01/14
 * @see CacheManager
 */
public interface Cache extends org.springframework.cache.Cache
{

    // void put(Object key, Object value, CacheParams cacheParams);

    /**
     * 通过key和value添加到当前缓存中，支持ttl
     *
     * @param key
     *            缓存key
     * @param value
     *            缓存结果
     * @param ttl
     *            ttl值
     * @param unit
     *            ttl单位
     */
    void /* TODO 返回旧值 */ put(Object key, Object value, long ttl, TimeUnit unit);

    /**
     *
     * @param key
     *            缓存key
     * @param value
     *            缓存结果
     * @param ttl
     *            ttl值
     * @param unit
     *            ttl单位
     * @param randomMillis
     *            ttl随机增量范围
     */
    void put(Object key, Object value, long ttl, TimeUnit unit, long randomMillis);

    /**
     *
     * @param key
     *            缓存key
     * @param value
     *            缓存结果
     * @param ttl
     *            ttl值
     * @param unit
     *            ttl单位
     * @return 缓存结果对象，如果缓存已存在则返回，不存在则返回null
     */
    ValueWrapper putIfAbsent(Object key, Object value, long ttl, TimeUnit unit);

    /**
     *
     * @param key
     *            缓存key
     * @param value
     *            缓存结果
     * @param ttl
     *            ttl值
     * @param unit
     *            ttl单位
     * @param randomMillis
     *            ttl随机增量范围
     * @return 缓存结果对象，如果缓存已存在则返回，不存在则返回null
     */
    ValueWrapper putIfAbsent(Object key, Object value, long ttl, TimeUnit unit, long randomMillis);

    /**
     * 延长缓存，ttl和unit在某些缓存下不一定有效
     * 
     * @param key
     *            缓存key
     * @param ttl
     *            ttl值
     * @param unit
     *            ttl单位
     */
    void touch(Object key, long ttl, TimeUnit unit);

    /**
     * TODO return type
     * 
     * 获取当前缓存的所有结果
     * 
     * @return 当前缓存的所有结果
     */
    ValueWrapper getAll();

    /**
     * 获取当前缓存的缓存类型
     * 
     * @return 缓存类型
     */
    CacheType getCacheType();

    /**
     * 通过锁的名称获取当前缓存的全局锁
     * 
     * @param lockName
     *            锁名称
     * @return 当前缓存的全局锁
     */
    Lock getLock(Object lockName);

    /**
     * 通过锁的名称获取当前缓存的全局读写锁
     * 
     * @param lockName
     *            锁名称
     * @return 当前缓存的全局读写锁
     */
    ReadWriteLock getReadWriteLock(Object lockName);
}

package com.hyf.cache;

import com.hyf.cache.impl.constants.CacheKeyConstants;

/**
 * 缓存key对象
 * 
 * @author baB_hyf
 * @date 2022/02/15
 */
public interface CacheKey extends Lockable<CacheKey>
{

    /**
     * 序列化使用
     * 
     * @return 序列化缓存key
     */
    @Override
    String toString();

    /**
     * 获取缓存key的版本
     * 
     * @return 缓存key的版本
     */
    String getVersion();

    /**
     * 获取缓存key的类型
     *
     * @return 缓存key的类型
     */
    String getType();

    /**
     * 获取缓存key的应用名称
     *
     * @return 缓存key的应用名称
     */
    String getApplication();

    /**
     * 获取缓存key的租户
     *
     * @return 缓存key的租户
     */
    String getTenant();

    /**
     * 获取缓存key的名称
     *
     * @return 缓存key的名称
     */
    String getCacheName();

    @Override
    default String getLockName() {
        return this.toString() + CacheKeyConstants.LOCK_SUFFIX;
    }

    @Override
    default int compareTo(CacheKey o) {
        return this.getLockName().compareTo(o.getLockName());
    }
}

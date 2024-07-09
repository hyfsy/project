package com.hyf.cache;

import com.hyf.cache.enums.CacheType;

/**
 * 缓存操作的后处理器
 * 
 * @author baB_hyf
 * @date 2022/01/14
 * @see CacheProcessor
 * @see CacheProcessChain
 */
public interface CachePostProcessor
{

    /**
     * 缓存操作的前处理，可控制是否继续执行当前缓存
     * 
     * @param cacheType
     *            当前操作的缓存类型
     * @param context
     *            缓存处理上下文
     * @return 继续执行缓存返回true，否则返回false
     */
    boolean postProcessBeforeCacheOperation(CacheType cacheType, CacheProcessContext context) throws CacheException;

    /**
     * 缓存操作的后处理
     *
     * @param cacheType
     *            当前操作的缓存类型
     * @param context
     *            缓存处理上下文
     * @param value
     *            缓存返回结果
     */
    CacheValueWrapper postProcessAfterCacheOperation(CacheType cacheType, CacheProcessContext context,
            CacheValueWrapper value) throws CacheException;

    /**
     * 业务方法的前处理，可控制是否继续执行当前业务方法
     *
     * @param context
     *            缓存处理上下文
     * @return 继续查询数据库返回true，否则返回false
     */
    boolean postProcessBeforeInvokeOperation(CacheProcessContext context) throws Exception;

    /**
     * 业务方法的后处理
     *
     * @param context
     *            缓存处理上下文
     * @param value
     *            方法返回结果
     */
    CacheValueWrapper postProcessAfterInvokeOperation(CacheProcessContext context, CacheValueWrapper value)
            throws Exception;
}

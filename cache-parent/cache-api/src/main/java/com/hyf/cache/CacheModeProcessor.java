package com.hyf.cache;

import java.lang.reflect.Method;

import com.hyf.cache.enums.CacheMode;

/**
 * 缓存模式处理器，对应执行不同的缓存逻辑
 * 
 * @author baB_hyf
 * @date 2022/01/14
 */
public interface CacheModeProcessor
{

    /**
     * 指定是否支持给定的缓存模式
     * 
     * @param cacheMode
     *            缓存模式
     * @return 支持返回true，否则返回false
     */
    boolean supportCacheMode(CacheMode cacheMode);

    /**
     * 进行实际的缓存处理
     *
     * @param target
     *            目标对象
     * @param method
     *            方法对象
     * @param args
     *            方法参数对象
     * @param context
     *            缓存处理上下文
     * @return 方法执行结果 or 缓存结果
     * @throws CacheException
     *             缓存相关异常
     */
    Object execute(Object target, Method method, Object[] args, CacheProcessContext context) throws CacheException;
}

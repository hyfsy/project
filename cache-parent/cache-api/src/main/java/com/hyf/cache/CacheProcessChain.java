package com.hyf.cache;

/**
 * 缓存处理器链，缓存处理器的执行入口
 * 
 * @author baB_hyf
 * @date 2022/01/14
 * @see CacheProcessor
 * @see CachePostProcessor
 */
public interface CacheProcessChain
{

    /**
     * 执行缓存链的下一次处理
     * 
     * @param context
     *            缓存处理上下文
     * @return 方法执行结果 or 缓存结果
     * @throws CacheException
     *             缓存相关异常
     */
    CacheValueWrapper execute(CacheProcessContext context) throws CacheException;

    /**
     * TODO 缓存同步处理
     * 
     * @param context
     *            缓存处理上下文
     * @param value
     *            方法执行结果 or 缓存结果
     * @throws CacheException
     *             缓存相关异常
     */
    void sync(CacheProcessContext context, CacheValueWrapper value) throws CacheException;
}

package com.hyf.cache;

import com.hyf.cache.enums.CacheType;

/**
 * 缓存处理器，执行核心的缓存处理流程
 * 
 * @author baB_hyf
 * @date 2022/01/14
 * @see CacheProcessChain
 */
public interface CacheProcessor
{

    /**
     * 设置缓存处理相关的配置
     *
     * @param configurer
     *            缓存处理流程配置
     */
    void setCacheProcessConfigurer(CacheProcessConfigurer configurer);

    /**
     * 校验是否执行当前处理器
     *
     * @param context
     *            缓存处理上下文
     * @return 支持返回true，否则返回false
     */
    boolean support(CacheProcessContext context);

    /**
     * 获取当前执行的缓存类型
     * 
     * @return 当前执行的缓存类型
     */
    CacheType getCacheType();

    /**
     * 缓存执行流程继续下一步操作
     *
     * @param context
     *            缓存处理上下文
     * @param chain
     *            缓存处理链
     * @return 方法执行结果 or 缓存结果
     * @throws CacheException
     *             缓存相关异常
     */
    CacheValueWrapper execute(CacheProcessContext context, CacheProcessChain chain) throws CacheException;

    // TODO 待定
    void sync(CacheProcessContext context, CacheValueWrapper value, CacheProcessChain chain) throws CacheException;
}

package com.hyf.cache.impl.mode;

import java.lang.reflect.Method;

import com.hyf.cache.CacheException;
import com.hyf.cache.CacheProcessContext;

/**
 * 自定义的缓存处理模式的接口
 *
 * @author baB_hyf
 * @date 2022/02/10
 */
public interface CustomModeProcessor
{

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

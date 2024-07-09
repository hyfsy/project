package com.hyf.cache;

import java.lang.reflect.Method;

/**
 * 方法调用上下文对象
 * 
 * @author baB_hyf
 * @date 2022/02/16
 * @see CacheProcessContext
 */
public interface InvocationContext
{

    /**
     * 获取当前调用的目标对象
     * 
     * @return 当前调用的目标对象
     */
    Object getTarget();

    /**
     * 获取当前调用的目标对象类型
     *
     * @return 当前调用的目标对象类型
     */
    Class<?> getTargetClass();

    /**
     * 获取当前调用的代理方法对象
     *
     * @return 当前调用的代理方法对象
     */
    Method getMethod();

    /**
     * 获取当前调用的方法对象
     *
     * @return 当前调用的方法对象
     */
    Method getTargetMethod();

    /**
     * 获取目标方法调用的参数
     *
     * @return 目标方法调用的参数，或空数组
     */
    Object[] getArgs();

    /**
     * 获取目标方法调用的结果
     *
     * @return 目标方法调用的结果
     */
    Object getResult();
}

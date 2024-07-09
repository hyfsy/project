package com.hyf.cache;

/**
 * 提供调用上下文的方法调用结果配置
 * 
 * @author baB_hyf
 * @date 2022/02/16
 * @see CacheProcessContext
 */
public interface ConfigurableInvocationContext extends InvocationContext
{

    /**
     * 设置目标方法调用结果
     * 
     * @param result
     *            目标方法调用结果
     */
    void setResult(Object result);
}

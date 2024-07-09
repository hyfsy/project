package com.hyf.cache.impl.key;

import com.hyf.cache.impl.constants.CacheKeyConstants;

import java.lang.reflect.Method;

/**
 * @author baB_hyf
 * @date 2022/02/08
 */
public class SpringCacheKey extends AbstractCacheKey
{

    private final Class<?> targetClass;
    private final Method targetMethod;
    private final Object[] args;

    public SpringCacheKey(String cacheName, Class<?> targetClass, Method targetMethod, Object... params) {
        super(cacheName);
        this.targetClass = targetClass;
        this.targetMethod = targetMethod;
        this.args = params.clone();
    }

    public Class<?> getTargetClass() {
        return targetClass;
    }

    public Method getTargetMethod() {
        return targetMethod;
    }

    public Object[] getArgs() {
        return args;
    }

    @Override
    protected String custom() {
        return String.join(":", getType(), CMA.toString(getTargetClass(), getTargetMethod(), getArgs()));
    }

    @Override
    public String getType() {
        return CacheKeyConstants.TYPE_SPRING;
    }
}

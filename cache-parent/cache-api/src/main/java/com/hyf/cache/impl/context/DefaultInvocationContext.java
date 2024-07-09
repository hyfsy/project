package com.hyf.cache.impl.context;

import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import org.springframework.aop.framework.AopProxyUtils;
import org.springframework.aop.support.AopUtils;
import org.springframework.core.BridgeMethodResolver;
import org.springframework.util.ObjectUtils;

import com.hyf.cache.ConfigurableInvocationContext;
import com.hyf.cache.InvocationContext;

/**
 * 方法调用上下文对象默认实现
 * 
 * @author baB_hyf
 * @date 2022/02/09
 * @see com.hyf.cache.CacheProcessContext
 */
public class DefaultInvocationContext implements ConfigurableInvocationContext
{

    private final Object target;

    private final Class<?> targetClass;

    private final Method method;

    private final Method targetMethod;

    private final Object[] args;

    private Object result;

    public DefaultInvocationContext(Object target, Method method, Object[] args) {
        this(target, method, args, null);
    }

    public DefaultInvocationContext(Object target, Method method, Object[] args, Object result) {
        this.target = target;
        this.targetClass = AopProxyUtils.ultimateTargetClass(target);
        this.method = BridgeMethodResolver.findBridgedMethod(method);
        this.targetMethod = (!Proxy.isProxyClass(this.targetClass)
                ? AopUtils.getMostSpecificMethod(method, this.targetClass)
                : this.method);
        this.args = extractArgs(this.method, args);
        this.result = result;
    }

    public static InvocationContext simple(Object target, Method method, Object[] args) {
        return new DefaultInvocationContext(target, method, args);
    }

    private Object[] extractArgs(Method method, Object[] args) {
        if (!method.isVarArgs()) {
            return args;
        }
        Object[] varArgs = ObjectUtils.toObjectArray(args[args.length - 1]);
        Object[] combinedArgs = new Object[args.length - 1 + varArgs.length];
        System.arraycopy(args, 0, combinedArgs, 0, args.length - 1);
        System.arraycopy(varArgs, 0, combinedArgs, args.length - 1, varArgs.length);
        return combinedArgs;
    }

    @Override
    public Object getTarget() {
        return target;
    }

    @Override
    public Class<?> getTargetClass() {
        return targetClass;
    }

    @Override
    public Method getMethod() {
        return method;
    }

    @Override
    public Method getTargetMethod() {
        return targetMethod;
    }

    @Override
    public Object[] getArgs() {
        return args;
    }

    @Override
    public Object getResult() {
        return result;
    }

    @Override
    public void setResult(Object result) {
        this.result = result;
    }
}

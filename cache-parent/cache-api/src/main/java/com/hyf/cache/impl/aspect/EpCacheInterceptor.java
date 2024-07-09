package com.hyf.cache.impl.aspect;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.aop.framework.AopProxyUtils;
import org.springframework.cache.interceptor.CacheAspectSupport;
import org.springframework.cache.interceptor.CacheOperation;
import org.springframework.cache.interceptor.CacheOperationInvoker;
import org.springframework.cache.interceptor.CacheOperationSource;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import com.hyf.cache.BaseCacheOperation;
import com.hyf.cache.CacheELContext;
import com.hyf.cache.CacheModeProcessor;
import com.hyf.cache.CacheOperationContexts;
import com.hyf.cache.CacheProcessContext;
import com.hyf.cache.enums.CacheMode;
import com.hyf.cache.impl.context.DefaultCacheProcessContext;
import com.hyf.cache.impl.operation.DefaultCacheOperationContexts;
import com.hyf.cache.impl.properties.CacheProperties;

/**
 * 自定义缓存切面，走自定义的扩展逻辑
 *
 * @author baB_hyf
 * @date 2022/02/08
 */
public class EpCacheInterceptor extends CacheAspectSupport implements MethodInterceptor, Serializable
{

    /** 缓存配置 */
    @Resource
    protected CacheProperties cacheProperties;

    /** 缓存模式的处理器列表，根据配置走不同的逻辑 */
    @Resource
    private List<CacheModeProcessor> cacheModeProcessorList = new ArrayList<>();

    @Override
    public final Object invoke(MethodInvocation invocation) throws Throwable {

        CacheOperationInvoker aopAllianceInvoker = getOperationInvoker(invocation);

        Object target = invocation.getThis();
        Assert.state(target != null, "Target must not be null");
        Method method = invocation.getMethod();
        Object[] args = invocation.getArguments();

        try {
            return execute(aopAllianceInvoker, target, method, args);
        }
        catch (CacheOperationInvoker.ThrowableWrapper th) {
            throw th.getOriginal();
        }
    }

    /**
     * 获取包装业务操作的对象
     * 
     * @param invocation
     *            方法调用对象xxx
     * @return 封装业务操作的对象
     */
    protected CacheOperationInvoker getOperationInvoker(MethodInvocation invocation) {
        return () -> {
            try {
                // 业务调用
                return invocation.proceed();
            }
            catch (Throwable ex) {
                throw new CacheOperationInvoker.ThrowableWrapper(ex);
            }
            // TODO 清理用户填充的数据，移到使用时再删除
            finally {
                CacheELContext.remove();
            }
        };
    }

    @Override
    protected Object execute(CacheOperationInvoker invoker, Object target, Method method, Object[] args) {

        // 初始化校验逻辑，暂时删除
        // Check whether aspect is enabled (to cope with cases where the AJ is pulled in
        // automatically)
        // if (this.initialized) {

        Class<?> targetClass = AopProxyUtils.ultimateTargetClass(target);
        // 获取缓存操作的元数据源
        CacheOperationSource cacheOperationSource = getCacheOperationSource();
        if (cacheOperationSource != null) {
            // 获取当前方法支持的缓存操作
            Collection<CacheOperation> operations = cacheOperationSource.getCacheOperations(method, targetClass);
            if (!CollectionUtils.isEmpty(operations)) {
                return execute(invoker, target, method, args, createCacheOperationContexts(operations, target, method, args, targetClass));
            }
        }
        // }

        return invoker.invoke();
    }

    /**
     * 获取 {@link CacheModeProcessor}对象进行实际的缓存处理逻辑
     *
     * @param invoker
     *            业务方法调用对象
     * @param target
     *            目标对象
     * @param method
     *            当前执行的方法
     * @param args
     *            当前执行的方法参数
     * @param contexts
     *            缓存操作上下文s对象
     * @return 方法返回结果 or 缓存结果
     */
    protected Object execute(final CacheOperationInvoker invoker, Object target, Method method, Object[] args, CacheOperationContexts contexts) {
        return getCacheModeProcessor().execute(target, method, args, createCacheProcessContext(invoker, target, method, args, contexts));
    }

    /**
     * 根据配置的缓存模式获取缓存模式处理器
     *
     * @return 缓存模式处理器
     */
    protected CacheModeProcessor getCacheModeProcessor() {
        CacheMode cacheMode = cacheProperties.getCacheMode();
        return cacheModeProcessorList.stream().filter(processor -> processor.supportCacheMode(cacheMode)).findFirst().orElseThrow(() -> new RuntimeException("Cannot find cache mode processor to process: " + cacheMode.name()));
    }

    /**
     * 通过当前方法支持的缓存操作构建缓存操作上下文s对象
     *
     * @param operations
     *            当前方法支持的缓存操作列表
     * @param target
     *            目标对象
     * @param method
     *            当前执行的方法
     * @param args
     *            当前执行的方法参数
     * @param targetClass
     *            目标类
     * @return 缓存操作上下文s对象
     */
    protected CacheOperationContexts createCacheOperationContexts(Collection<CacheOperation> operations, Object target, Method method, Object[] args, Class<?> targetClass) {
        // TODO 支持CacheOperation操作，不局限于BaseCacheOperation
        Collection<BaseCacheOperation> baseCacheOperations = operations.stream().filter(BaseCacheOperation.class::isInstance).map(BaseCacheOperation.class::cast).collect(Collectors.toList());
        return new DefaultCacheOperationContexts(baseCacheOperations, method, args, target, targetClass);
    }

    /**
     * 构建缓存处理上下文对象
     *
     * @param invoker
     *            业务方法调用对象
     * @param target
     *            目标对象
     * @param method
     *            当前执行的方法
     * @param args
     *            当前执行的方法参数
     * @param contexts
     *            缓存操作上下文s对象
     * @return 缓存处理上下文
     */
    protected CacheProcessContext createCacheProcessContext(CacheOperationInvoker invoker, Object target, Method method, Object[] args, CacheOperationContexts contexts) {
        return new DefaultCacheProcessContext(invoker, target, method, args, contexts);
    }

    @Override
    public void afterSingletonsInstantiated() {
        // ignore validate
        // super.afterSingletonsInstantiated();
    }
}

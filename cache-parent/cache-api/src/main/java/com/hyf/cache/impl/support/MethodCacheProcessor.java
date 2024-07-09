package com.hyf.cache.impl.support;

import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;

import org.springframework.cache.support.NullValue;
import org.springframework.cache.support.SimpleValueWrapper;
import org.springframework.util.Assert;

import com.hyf.cache.*;
import com.hyf.cache.enums.CacheType;
import com.hyf.cache.impl.context.DynamicCacheConfigContext;
import com.hyf.cache.impl.key.CacheBin;
import com.hyf.cache.impl.key.lock.CacheKeyLockUtils;
import com.hyf.cache.impl.operation.CacheEvictOperation;
import com.hyf.cache.impl.operation.CacheableOperation;
import com.hyf.cache.impl.utils.ApplicationContextUtils;
import com.hyf.cache.impl.utils.StringUtils;

/**
 * 方法缓存的执行逻辑
 *
 * @author baB_hyf
 * @date 2022/02/09
 * @see CacheableOperation
 * @see CacheEvictOperation
 */
public class MethodCacheProcessor extends BaseCacheProcessor
{

    @Override
    public CacheValueWrapper doExecute(CacheProcessContext context, CacheProcessChain chain) throws CacheException {

        CacheValueWrapper returnValue = null;
        org.springframework.cache.Cache.ValueWrapper cacheValue = null;

        CacheOperationContexts cacheOperationContexts = context.getCacheOperationContexts();

        // 获取缓存操作选项 -> 注解配置

        Collection<CacheOperationContext<BaseCacheOperation>> cacheableOperationContexts = cacheOperationContexts
                .get(CacheableOperation.class);

        Collection<CacheOperationContext<BaseCacheOperation>> cacheEvictOperationContexts = cacheOperationContexts
                .get(CacheEvictOperation.class);

        // 启动检查重复情况
        // int reuse = reuseModifiableOperationContextList.size() +
        // reuseCacheableOperationContextList.size();
        // int common = cacheableOperationContextList.size() +
        // cacheEvictOperationContextList.size();
        // if (reuse != 0 && common != 0) {
        // throw new RuntimeException("annotation illegal use: " +
        // context.getInvocationContext().getMethod().toString());
        // }

        // 1. 驱逐缓存
        MethodCacheEvictor methodCacheEvictor = new MethodCacheEvictor(getConfigurer());
        methodCacheEvictor.evict(context.getInvocationContext(), cacheEvictOperationContexts, true);

        // 2. 先找到所有需要加锁的key

        // lock keys
        Map<Cache, List<Object>> allCacheKeyMap = new HashMap<>();
        // all keys
        Map<CacheOperationContext<BaseCacheOperation>, CacheBin> opsMap = new HashMap<>();

        // 遍历Cacheable操作
        for (CacheOperationContext<BaseCacheOperation> cacheOperationContext : cacheableOperationContexts) {
            CacheableOperation operation = (CacheableOperation) cacheOperationContext.getOperation();

            // TODO condition

            // 是否进行缓存
            if (doCacheOps(operation.getCacheType())) {
                // 解析对应的缓存使用信息，方便下方操作
                CacheBin cacheBin = getCacheBin(cacheOperationContext, context.getInvocationContext().getResult());

                // 是否加全局锁保证强一致
                if (operation.isForceConsistency()) {
                    allCacheKeyMap.putAll(cacheBin.getOrigin());
                }

                opsMap.put(cacheOperationContext, cacheBin);
            }
        }

        // 构建全局锁
        Lock lock = CacheKeyLockUtils.getLock(allCacheKeyMap);

        // 必要时加锁
        lock.lock();

        try {

            org.springframework.cache.Cache.ValueWrapper cacheHit = null;

            // 3. 查找缓存
            for (Map.Entry<CacheOperationContext<BaseCacheOperation>, CacheBin> entry : opsMap.entrySet()) {
                CacheBin cacheBin = entry.getValue();

                // 通过所有的缓存key查找缓存
                cacheHit = cacheBin.getCacheKeyBins().stream().map(bs -> bs.getCacheKeys().stream()
                        // 通过缓存key获取缓存结果
                        .map(k -> getFromCaches(cacheBin.getCaches(), k))
                        // 过滤不存在的
                        .filter(Objects::nonNull).findAny().orElse(null)).filter(Objects::nonNull).findAny()
                        .orElse(null);

                // 缓存命中，缓存结果转换及设置方法返回值
                if (cacheHit != null) {

                    // 缓存结果转换为方法返回结果
                    cacheHit = returnResultConvert(context.getInvocationContext(), entry.getKey(), cacheHit);

                    // 设置调用上下文的方法返回结果
                    InvocationContext invocationContext = context.getInvocationContext();
                    if (invocationContext instanceof ConfigurableInvocationContext) {
                        ((ConfigurableInvocationContext) invocationContext).setResult(cacheHit.get());
                    }

                    break;
                }
            }

            // 4. 业务调用

            // 缓存命中，直接返回
            if (cacheHit != null) {
                returnValue = new CacheValueWrapper(getCacheType(),
                        wrapCacheValue(context.getInvocationContext().getMethod(), cacheHit.get()));
                cacheValue = cacheHit;
            }
            // 无缓存命中，继续处理
            else {
                returnValue = chain.execute(context);
                cacheValue = new CacheValueWrapper(getCacheType(), unwrapReturnValue(returnValue.get()));
            }

            // 5. 设置缓存
            if (cacheHit == null) {

                for (CacheOperationContext<BaseCacheOperation> cacheOperationContext : cacheableOperationContexts) {
                    CacheableOperation operation = (CacheableOperation) cacheOperationContext.getOperation();

                    if (!doCacheOps(operation.getCacheType())) {
                        continue;
                    }

                    try {
                        // 供CacheManager/Cache使用
                        DynamicCacheConfigContext.setDynamicCreate(operation.isDynamicCreate());
                        DynamicCacheConfigContext.setAllowNull(operation.isAllowNull());

                        CacheParams cacheParams = new CacheParams();
                        cacheParams.setTtl(operation.getTtl());
                        cacheParams.setUnit(operation.getUnit());
                        cacheParams.setRandom(operation.isRandom());
                        cacheParams
                                .setRandomMillis(getDefaultRandomMillis(cacheParams.getTtl(), cacheParams.getUnit()));

                        // 方法返回值转换为缓存结果
                        cacheValue = cacheResultConvert(context.getInvocationContext(), cacheOperationContext,
                                cacheValue);

                        // 获取缓存key关联信息
                        CacheBin cacheBin = getCacheBin(cacheOperationContext,
                                context.getInvocationContext().getResult());

                        for (CacheBin.CacheKeyBin cacheKeyBin : cacheBin.getCacheKeyBins()) {

                            List<Object> cacheKeys = cacheKeyBin.getCacheKeys();
                            for (Object key : cacheKeys) {
                                // 添加缓存
                                putByCaches(cacheBin.getCaches(), key, cacheValue.get(), cacheParams);
                            }
                        }
                    }
                    // 动态缓存配置
                    finally {
                        DynamicCacheConfigContext.remove();
                    }
                }
            }
        }
        finally {
            lock.unlock();
        }

        // 6. 驱逐缓存
        methodCacheEvictor.evict(context.getInvocationContext(), cacheEvictOperationContexts, false);

        return returnValue;
    }

    @Override
    public boolean support(CacheProcessContext context) {
        CacheOperationContexts cacheOperationContexts = context.getCacheOperationContexts();
        // 仅支持方法缓存相关选项配置
        return cacheOperationContexts.get(CacheableOperation.class).size() > 0
                || cacheOperationContexts.get(CacheEvictOperation.class).size() > 0;
    }

    @Override
    public void sync(CacheProcessContext context, CacheValueWrapper value, CacheProcessChain chain)
            throws CacheException {

    }

    private CacheBin getCacheBin(CacheOperationContext<BaseCacheOperation> cacheOperationContext, Object result) {

        CacheBin cacheBin = new CacheBin();

        BaseCacheOperation operation = cacheOperationContext.getOperation();

        // 通过缓存操作获取对应的缓存
        Collection<Cache> caches = resolveCaches(cacheOperationContext);
        for (Cache cache : caches) {
            // 获取所有的缓存key
            List<Object> cacheKeys = cacheOperationContext.getCacheKey(cache, result);
            CacheBin.CacheKeyBin cacheKeyBin = new CacheBin.CacheKeyBin();
            cacheKeyBin.setCache(cache);
            cacheKeyBin.setCacheKeys(cacheKeys);
            cacheBin.addCacheKeyBin(cacheKeyBin);
            cacheBin.setCacheOperationContext(cacheOperationContext);
            cacheBin.setOperation(operation);
        }
        return cacheBin;
    }

    /**
     * TODO 放其他地方去
     *
     * 缓存随机ttl增量
     */
    private long getDefaultRandomMillis(long ttl, TimeUnit unit) {
        return TimeUnit.SECONDS.toMillis(3);
    }

    /**
     * option对象的包装
     * 
     * @param method
     *            目标方法
     * @param cacheValue
     *            缓存结果
     * @return 方法返回值
     */
    private Object wrapCacheValue(Method method, Object cacheValue) {
        if (method.getReturnType() == Optional.class
                && (cacheValue == null || cacheValue.getClass() != Optional.class)) {
            return Optional.ofNullable(cacheValue);
        }
        return cacheValue;
    }

    /**
     * option对象的解包装
     *
     * @param obj
     *            方法返回值
     * @return obj or unwrap obj
     */
    private Object unwrapReturnValue(Object obj) {
        if (obj instanceof Optional) {
            Optional<?> optional = (Optional<?>) obj;
            if (!optional.isPresent()) {
                return null;
            }
            Object result = optional.get();
            Assert.isTrue(!(result instanceof Optional), "Multi-level Optional usage not supported");
            return result;
        }
        return obj;
    }

    /**
     * 校验是否执行缓存操作
     *
     * @param cacheTypes
     *            缓存类型
     * @return 需要缓存操作返回true，否则返回false
     */
    private boolean doCacheOps(CacheType[] cacheTypes) {
        boolean doCacheOps = false;
        if (cacheTypes.length == 0) {
            doCacheOps = true;
        }
        else {
            for (CacheType type : cacheTypes) {
                if (type.equals(getCacheType())) {
                    doCacheOps = true;
                }
            }
        }
        return doCacheOps;
    }

    private CacheResultConverter<Object> getCacheResultConverter(Method method, String hint) {
        // TODO
        Type genericReturnType = method.getGenericReturnType();
        return null;
    }

    /**
     * 通过缓存对象列表获取存在缓存结果值的缓存结果
     * 
     * @param caches
     *            缓存对象列表
     * @param key
     *            缓存key
     * @return 缓存结果 or null
     */
    private org.springframework.cache.Cache.ValueWrapper getFromCaches(Collection<? extends Cache> caches, Object key) {
        org.springframework.cache.Cache.ValueWrapper cacheHit = null;
        for (Cache cache : caches) {
            cacheHit = cache.get(key);
            if (cacheHit != null) {
                break;
            }
        }
        return cacheHit;
    }

    /**
     * 向所有缓存对象列表内添加指定缓存
     * 
     * @param caches
     *            缓存对象列表
     * @param key
     *            缓存key
     * @param value
     *            缓存结果
     * @param params
     *            缓存动态配置参数
     * @return null
     */
    private Object putByCaches(Collection<? extends Cache> caches, Object key, Object value, CacheParams params) {
        long ttl = params.getTtl();
        TimeUnit unit = params.getUnit();

        for (Cache cache : caches) {
            if (params.isRandom()) {
                cache.put(key, value, ttl, unit);
            }
            else {
                cache.put(key, value, ttl, unit, params.getRandomMillis());
            }
        }

        // TODO putIfAbsent

        return null;
    }

    /**
     * 解析缓存，通过缓存操作上下文获取需要操作的缓存
     *
     * @param cacheOperationContext
     *            缓存操作上下文
     * @return 缓存对象列表
     */
    private Collection<Cache> resolveCaches(CacheOperationContext<BaseCacheOperation> cacheOperationContext) {
        BaseCacheOperation operation = cacheOperationContext.getOperation();
        String cacheResolver = operation.getCacheResolver();

        if (!StringUtils.isBlank(cacheResolver)) {
            return CacheAdapter.adapt(ApplicationContextUtils
                    .getBean(cacheResolver, org.springframework.cache.interceptor.CacheResolver.class)
                    .resolveCaches(cacheOperationContext));
        }

        return getCacheResolver().resolveCaches(cacheOperationContext);
    }

    /**
     * 方法返回值处理，搞成缓存结果
     *
     * @param invocationContext
     *            调用上下文
     * @param cacheOperationContext
     *            缓存处理上下文
     * @param cacheHit
     *            缓存结果
     * @return 方法返回值的包装
     */
    private org.springframework.cache.Cache.ValueWrapper returnResultConvert(InvocationContext invocationContext,
            CacheOperationContext<BaseCacheOperation> cacheOperationContext,
            org.springframework.cache.Cache.ValueWrapper cacheHit) {
        BaseCacheOperation baseCacheOperation = cacheOperationContext.getOperation();

        if (baseCacheOperation instanceof CacheableOperation) {
            CacheableOperation operation = (CacheableOperation) baseCacheOperation;

            String cacheResultConverterString = operation.getCacheResultConverter();
            if (!StringUtils.isBlank(cacheResultConverterString)) {
                CacheResultConverter<Object> cacheResultConverter = getCacheResultConverter(
                        invocationContext.getMethod(), cacheResultConverterString);
                if (cacheResultConverter != null) {
                    cacheHit = wrapValue(
                            cacheResultConverter.convertReturnResult(invocationContext.getArgs(), cacheHit.get()));
                }
            }
        }

        return cacheHit;
    }

    /**
     * 缓存结果处理，搞成方法返回值
     *
     * @param invocationContext
     *            调用上下文
     * @param cacheOperationContext
     *            缓存处理上下文
     * @param cacheHit
     *            缓存结果
     * @return 方法返回值的包装
     */
    private org.springframework.cache.Cache.ValueWrapper cacheResultConvert(InvocationContext invocationContext,
            CacheOperationContext<BaseCacheOperation> cacheOperationContext,
            org.springframework.cache.Cache.ValueWrapper cacheHit) {

        BaseCacheOperation baseCacheOperation = cacheOperationContext.getOperation();

        if (baseCacheOperation instanceof CacheableOperation) {
            CacheableOperation operation = (CacheableOperation) baseCacheOperation;

            String cacheResultConverterString = operation.getCacheResultConverter();
            if (!StringUtils.isBlank(cacheResultConverterString)) {
                CacheResultConverter<Object> cacheResultConverter = getCacheResultConverter(
                        invocationContext.getMethod(), cacheResultConverterString);
                if (cacheResultConverter != null) {
                    cacheHit = wrapValue(
                            cacheResultConverter.convertCacheResult(invocationContext.getArgs(), cacheHit.get()));
                }
            }
        }

        return cacheHit;
    }

    /**
     * 值结果包装为缓存结果对象
     *
     * @param value
     *            值对象
     * @return 缓存结果对象
     */
    private org.springframework.cache.Cache.ValueWrapper wrapValue(Object value) {
        if (value == null || value instanceof NullValue) {
            return new SimpleValueWrapper(null);
        }
        else {
            return new SimpleValueWrapper(value);
        }
    }

    public CacheManager getCacheManager() {
        return getConfigurer().getCacheManager();
    }

    public CacheResolver getCacheResolver() {
        return getConfigurer().getCacheResolver();
    }
}

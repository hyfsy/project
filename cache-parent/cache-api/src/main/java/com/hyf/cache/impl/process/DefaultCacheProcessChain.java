package com.hyf.cache.impl.process;

import java.util.List;

import com.hyf.cache.CacheException;
import com.hyf.cache.CachePostProcessor;
import com.hyf.cache.CacheProcessChain;
import com.hyf.cache.CacheProcessContext;
import com.hyf.cache.CacheProcessor;
import com.hyf.cache.CacheValueWrapper;
import com.hyf.cache.enums.CacheType;

/**
 * 缓存处理器链默认实现
 *
 * @author baB_hyf
 * @date 2022/02/09
 * @see CacheProcessChainFactoryBean
 * @see CacheProcessor
 * @see CachePostProcessor
 */
public class DefaultCacheProcessChain implements CacheProcessChain
{

    protected final CacheValueWrapper[] cacheValueWrappers;
    private final List<CacheProcessor> cacheProcessors;
    private final List<CachePostProcessor> cachePostProcessors;
    private int pos = -1;

    public DefaultCacheProcessChain(List<CacheProcessor> cacheProcessors, List<CachePostProcessor> cachePostProcessors) {
        this.cacheProcessors = cacheProcessors;
        this.cachePostProcessors = cachePostProcessors;
        this.cacheValueWrappers = new CacheValueWrapper[cacheProcessors.size()];
    }

    @Override
    public CacheValueWrapper execute(CacheProcessContext context) throws CacheException {

        CacheValueWrapper cacheResult = null;

        // 执行业务方法
        if (pos == cacheProcessors.size() - 1) {
            try {
                return doInvoke(context);
            }
            catch (Exception e) {
                throw new CacheException("cache miss, invoke method failed");
            }
        }

        int cur = ++pos;

        // 执行缓存处理器的前处理和执行
        CacheProcessor processor = cacheProcessors.get(cur);
        if (applyCachePostProcessorsBeforeCacheOperation(context, processor)) {
            cacheResult = processor.execute(context, this);
        }

        // 缓存结果，方便传递
        cacheValueWrappers[cur] = cacheResult;

        // 执行缓存处理器的后处理
        cacheResult = applyCachePostProcessorsAfterCacheOperation(context, processor, cacheResult);

        return cacheResult;
    }

    @Override
    public void sync(CacheProcessContext context, CacheValueWrapper value) throws CacheException {

    }

    // private CacheValueWrapper getLastCacheValueWrapper(int cur) {
    // for (int i = cur; i >= 0; i--) {
    // CacheValueWrapper cacheValueWrapper = cacheValueWrappers.get(cur);
    // if (cacheValueWrapper != null) {
    // return cacheValueWrapper;
    // }
    // }
    //
    // return null;
    // }

    protected CacheValueWrapper doInvoke(CacheProcessContext context) throws Exception {
        CacheValueWrapper cacheValueWrapper = null;

        // 方法调用前处理
        // TODO 可能会发生不走用户的业务方法
        if (applyCachePostProcessorsBeforeInvokeOperation(context)) {
            Object result = context.getInvoker().invoke();
            cacheValueWrapper = new CacheValueWrapper(CacheType.NONE, result);
        }

        // 方法调用后处理
        cacheValueWrapper = applyCachePostProcessorsAfterInvokeOperation(context, cacheValueWrapper);

        return cacheValueWrapper;
    }

    protected boolean applyCachePostProcessorsBeforeCacheOperation(CacheProcessContext context, CacheProcessor cacheProcessor) {
        boolean pc = true;
        for (CachePostProcessor cachePostProcessor : cachePostProcessors) {
            pc &= cachePostProcessor.postProcessBeforeCacheOperation(cacheProcessor.getCacheType(), context);
        }
        return pc;
    }

    protected CacheValueWrapper applyCachePostProcessorsAfterCacheOperation(CacheProcessContext context,
            CacheProcessor cacheProcessor, CacheValueWrapper cacheResult) {
        for (CachePostProcessor cachePostProcessor : cachePostProcessors) {
            cacheResult = cachePostProcessor.postProcessAfterCacheOperation(cacheProcessor.getCacheType(), context,
                    cacheResult);
        }
        return cacheResult;
    }

    protected boolean applyCachePostProcessorsBeforeInvokeOperation(CacheProcessContext context) throws Exception {
        boolean bc = true;
        for (CachePostProcessor cachePostProcessor : cachePostProcessors) {
            bc &= cachePostProcessor.postProcessBeforeInvokeOperation(context);
        }
        return bc;
    }

    protected CacheValueWrapper applyCachePostProcessorsAfterInvokeOperation(CacheProcessContext context,
            CacheValueWrapper cacheResult) throws Exception {
        for (CachePostProcessor cachePostProcessor : cachePostProcessors) {
            cacheResult = cachePostProcessor.postProcessAfterInvokeOperation(context, cacheResult);
        }
        return cacheResult;
    }

}

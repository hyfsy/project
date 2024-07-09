package com.hyf.cache.impl.process;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.util.Assert;

import com.hyf.cache.*;
import com.hyf.cache.enums.CacheType;

/**
 * TODO 根据op路由
 * 
 * @author baB_hyf
 * @date 2022/03/03
 */
public class CompositeCacheProcessor implements CacheProcessor
{

    private final List<CacheProcessor> cacheProcessors;

    public CompositeCacheProcessor(List<CacheProcessor> cacheProcessors) {
        Assert.notEmpty(cacheProcessors, "CacheProcessors must not empty");
        cacheProcessors.stream().map(CacheProcessor::getCacheType)
                .collect(Collectors.reducing((t1, t2) -> t1 == t2 ? t1 : null))
                .orElseThrow(() -> new IllegalArgumentException("Processor CacheType is not the same"));

        this.cacheProcessors = cacheProcessors;
    }

    @Override
    public void setCacheProcessConfigurer(CacheProcessConfigurer configurer) {
        cacheProcessors.forEach(p -> p.setCacheProcessConfigurer(configurer));
    }

    @Override
    public boolean support(CacheProcessContext context) {
        return true;
    }

    @Override
    public CacheType getCacheType() {
        return cacheProcessors.get(0).getCacheType();
    }

    @Override
    public CacheValueWrapper execute(CacheProcessContext context, CacheProcessChain chain) throws CacheException {
        for (CacheProcessor cacheProcessor : cacheProcessors) {
            if (cacheProcessor.support(context)) {
                return cacheProcessor.execute(context, chain);
            }
        }
        return chain.execute(context);
    }

    @Override
    public void sync(CacheProcessContext context, CacheValueWrapper value, CacheProcessChain chain)
            throws CacheException {
        cacheProcessors.forEach(p -> p.sync(context, value, chain));
    }

    @Override
    public String toString() {
        return "CompositeCacheProcessor{cacheProcessors=" + cacheProcessors + '}';
    }
}

package com.hyf.cache;

import com.hyf.cache.enums.CacheType;

/**
 * 基于{@link CacheProcessConfigurer}配置的缓存处理器，提供不同缓存实现，复用缓存处理流程
 * 
 * @author baB_hyf
 * @date 2022/02/17
 * @see CacheProcessConfigurer
 */
public abstract class BaseCacheProcessor implements CacheProcessor
{

    protected CacheProcessConfigurer configurer;

    @Override
    public void setCacheProcessConfigurer(CacheProcessConfigurer configurer) {
        this.configurer = configurer;
    }

    public CacheProcessConfigurer getConfigurer() {
        return configurer;
    }

    @Override
    public final CacheValueWrapper execute(CacheProcessContext context, CacheProcessChain chain) throws CacheException {

        if (support(context)) {
            return doExecute(context, chain);
        }

        return chain.execute(context);
    }

    @Override
    public CacheType getCacheType() {
        return getConfigurer().getCacheManager().getCacheType();
    }

    protected abstract CacheValueWrapper doExecute(CacheProcessContext context, CacheProcessChain chain)
            throws CacheException;

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append('[');
        if (configurer.getCacheManager().getCacheType() != null) {
            sb.append(configurer.getCacheManager().getCacheType());
        }
        sb.append(']').append(' ').append(getClass().getSimpleName());
        // if (configurer.getCacheManager() != null) {
        // sb.append(" cacheManager:
        // [").append(configurer.getCacheManager().getClass().getName()).append("]");
        // }
        // if (configurer.getCacheManager() != null) {
        // sb.append(" cacheResolver:
        // [").append(configurer.getCacheResolver().getClass().getName()).append("]");
        // }
        return sb.toString();
    }
}

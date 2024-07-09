package com.hyf.cache.impl.support.caffeine;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.core.annotation.AnnotationAwareOrderComparator;

import com.hyf.cache.Cache;
import com.hyf.cache.CacheAdapter;
import com.hyf.cache.CacheManager;
import com.hyf.cache.enums.CacheType;
import com.hyf.cache.impl.context.DynamicCacheConfigContext;
import com.hyf.cache.impl.properties.CacheProperties;
import com.hyf.cache.impl.support.DynamicTTLCache;
import com.hyf.cache.impl.utils.ApplicationContextUtils;
import com.github.benmanes.caffeine.cache.CacheLoader;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.CaffeineSpec;

/**
 * 扩展CaffeineCacheManager的Cache对象创建，实现自己的CacheManager接口
 * 
 * @author baB_hyf
 * @date 2022/02/17
 * @see AdaptiveCaffeineCache
 */
public class AdaptiveCaffeineCacheManager extends CaffeineCacheManager implements CacheManager
{

    /**
     * 默认的CacheLoader key
     */
    public static final String GLOBAL_CACHE_LOADER_KEY = "";

    /**
     * caffeine缓存构建器的缓存，方便直接构造
     */
    private static final Map<String, Caffeine<Object, Object>> caffeineMap = new ConcurrentHashMap<>(32);

    /**
     * caffeine缓存加载器，方便配置使用
     */
    private static final Map<String, CacheLoader<Object, Object>> caffeineCacheLoaderMap = new ConcurrentHashMap<>(2);

    /**
     * 适配自动创建的配置
     * 
     * @param name
     *            缓存名称
     * @return 默认的缓存对象
     */
    @Override
    public org.springframework.cache.Cache getCache(String name) {
        Boolean dynamicCreate = DynamicCacheConfigContext.getDynamicCreate();
        org.springframework.cache.Cache cache = super.getCache(name);
        if (cache == null) {
            if (dynamicCreate != null && dynamicCreate) {
                cache = super.createCaffeineCache(name);
            }
            if (cache != null) {
                super.registerCustomCache(name, (com.github.benmanes.caffeine.cache.Cache<Object, Object>) cache.getNativeCache());
            }
        }
        return cache;
    }

    /**
     * 适配为根据不同的ttl创建不同缓存的功能
     *
     * @param name
     *            缓存名称
     * @param cache
     *            默认创建的缓存对象
     * @return 组合缓存，根据ttl分组
     */
    @Override
    protected org.springframework.cache.Cache adaptCaffeineCache(String name, com.github.benmanes.caffeine.cache.Cache<Object, Object> cache) {
        AdaptiveCaffeineCache defaultCaffeineCache = new AdaptiveCaffeineCache(name, cache);
        // for dynamic ttl
        return new DynamicTTLCache(defaultCaffeineCache, (ttl, unit) -> new AdaptiveCaffeineCache(name, getDefaultCacheByTTL(name, ttl, unit)));
    }

    /**
     * 针对于caffeine缓存，设置默认的缓存加载器
     * 
     * @param cacheLoader
     *            默认缓存加载器
     */
    @Override
    public void setCacheLoader(CacheLoader<Object, Object> cacheLoader) {
        setCacheLoader(GLOBAL_CACHE_LOADER_KEY, cacheLoader);
    }

    /**
     * 针对于caffeine缓存，设置自定义的缓存加载器
     * 
     * @param cacheLoader
     *            自定义缓存加载器
     */
    public void setCacheLoader(String cacheName, CacheLoader<Object, Object> cacheLoader) {
        caffeineCacheLoaderMap.putIfAbsent(cacheName, cacheLoader);
    }

    @Override
    protected com.github.benmanes.caffeine.cache.Cache<Object, Object> createNativeCaffeineCache(String name) {
        return getDefaultCache(name);
    }

    protected com.github.benmanes.caffeine.cache.Cache<Object, Object> getDefaultCache(String name) {
        return getDefaultCacheByTTL(name, -1, null);
    }

    /**
     * 根据ttl配置创建出不同的缓存
     *
     * @param name
     *            缓存名称
     * @param ttl
     *            ttl值
     * @param unit
     *            ttl单位
     * @return 原生caffeine缓存
     */
    protected com.github.benmanes.caffeine.cache.Cache<Object, Object> getDefaultCacheByTTL(String name, long ttl, TimeUnit unit) {

        // 默认caffeine的构建器模板
        Caffeine<Object, Object> caffeineBuilder = getDefaultCaffeineBuilder(name);

        // 动态的ttl设置
        if (ttl > 0) {
            caffeineBuilder.expireAfterWrite(ttl, unit);
        }

        CacheLoader<Object, Object> cacheLoader = getDefaultCacheLoader(name);
        return cacheLoader == null ? caffeineBuilder.build() : caffeineBuilder.build(cacheLoader);
    }

    protected CacheLoader<Object, Object> getDefaultCacheLoader(String cacheName) {
        return caffeineCacheLoaderMap.getOrDefault(cacheName, caffeineCacheLoaderMap.get(GLOBAL_CACHE_LOADER_KEY));
    }

    protected Caffeine<Object, Object> getDefaultCaffeineBuilder(String name) {

        List<CaffeineCacheBuilderSupplier> suppliers = getSuppliers();

        // 异步暂时不支持
        // if (isAsync()) {
        // return caffeineBuilder.buildAsync(cacheLoader);
        // }

        return caffeineMap.computeIfAbsent(name, k -> suppliers.stream().map(c -> c.get(name)).filter(Objects::nonNull).findAny().orElse(Caffeine.newBuilder()));
    }

    @Override
    public List<String> getCacheNameList() {
        return new ArrayList<>(getCacheNames());
    }

    @Override
    public Cache findCache(String cacheName) {
        return CacheAdapter.adapt(getCache(cacheName));
    }

    public Cache findCacheNotListener(String cacheName) {
        return CacheAdapter.adaptNotListener(getCache(cacheName));
    }

    @Override
    public CacheType getCacheType() {
        return CacheType.CAFFEINE;
    }

    /**
     * 获取caffeine构建器列表，返回缓存的构建器对象，默认存在的第一个
     * 
     * @return 构建器提供对象列表
     */
    private List<CaffeineCacheBuilderSupplier> getSuppliers() {
        List<CaffeineCacheBuilderSupplier> caffeineCacheBuilderSuppliers = new ArrayList<>(ApplicationContextUtils.getApplicationContext().getBeansOfType(CaffeineCacheBuilderSupplier.class).values());

        AnnotationAwareOrderComparator.sort(caffeineCacheBuilderSuppliers);

        return caffeineCacheBuilderSuppliers;
    }

    private boolean isAsync() {
        CacheProperties cacheProperties = ApplicationContextUtils.getApplicationContext().getBean(CacheProperties.class);
        return cacheProperties.getCaffeine().isAsync();
    }

    @Override
    public void setCaffeine(Caffeine<Object, Object> caffeine) {
        throw new UnsupportedOperationException("setCaffeine");
    }

    @Override
    public void setCacheSpecification(String cacheSpecification) {
        throw new UnsupportedOperationException("setCacheSpecification");
    }

    @Override
    public void setCaffeineSpec(CaffeineSpec caffeineSpec) {
        throw new UnsupportedOperationException("setCaffeineSpec");
    }
}

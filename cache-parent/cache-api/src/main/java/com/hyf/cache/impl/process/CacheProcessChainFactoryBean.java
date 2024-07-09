package com.hyf.cache.impl.process;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.annotation.Autowired;

import com.hyf.cache.CachePostProcessor;
import com.hyf.cache.CacheProcessChain;
import com.hyf.cache.CacheProcessor;
import com.hyf.cache.enums.CacheType;

/**
 * 缓存处理链工厂，缓存解析过的处理器列表及后处理器列表
 * 
 * @author baB_hyf
 * @date 2022/02/09
 * @see CacheProcessChain
 * @see CacheProcessor
 * @see CachePostProcessor
 * @see ChainOrderBuilder
 */
public class CacheProcessChainFactoryBean implements FactoryBean<CacheProcessChain>
{

    // cache
    @Autowired(required = false)
    private List<CacheProcessor> cacheProcessors = new ArrayList<>();
    @Autowired(required = false)
    private List<CachePostProcessor> cachePostProcessors = new ArrayList<>();

    // real use
    @Autowired(required = false)
    private ChainOrderBuilder chainOrderBuilder;
    private List<CacheProcessor> processors = new ArrayList<>();
    private List<CachePostProcessor> postProcessors = new ArrayList<>();

    @PostConstruct
    public void post() {
        processors = cacheProcessors;
        postProcessors = cachePostProcessors;
        buildByChainOrderBuilder();
    }

    @Override
    public CacheProcessChain getObject() throws Exception {
        return new DefaultCacheProcessChain(processors, postProcessors);
    }

    @Override
    public Class<?> getObjectType() {
        return CacheProcessChain.class;
    }

    @Override
    public boolean isSingleton() {
        return false;
    }

    private void buildByChainOrderBuilder() {
        // 默认缓存处理
        if (chainOrderBuilder == null) {
            return;
        }

        // 无需缓存处理
        CacheType[] cacheTypes = chainOrderBuilder.buildWithCacheType();
        if (cacheTypes == null || cacheTypes.length == 0 || containsType(cacheTypes, CacheType.NONE)) {
            processors = Collections.emptyList();
            postProcessors = Collections.emptyList();
            return;
        }

        // 默认缓存处理
        if (containsType(cacheTypes, CacheType.GENERIC)) {
            return;
        }

        // 排序指定缓存处理
        List<CacheProcessor> customCacheProcessors = new ArrayList<>();

        for (CacheType cacheType : cacheTypes) {
            switch (cacheType) {
                case SIMPLE:
                case EHCACHE:
                case CAFFEINE:
                case REDIS:
                    List<CacheProcessor> specifiedCacheProcessors = cacheProcessors.stream()
                            .filter(p -> p.getCacheType().equals(cacheType)).collect(Collectors.toList());
                    if (specifiedCacheProcessors.isEmpty()) {
                        throw new IllegalStateException("Processor instance not present: " + cacheType);
                    }
                    customCacheProcessors.addAll(specifiedCacheProcessors);
                    break;
                case CUSTOM:
                    CacheProcessor customProcessor = chainOrderBuilder.getCustomProcessor();
                    if (customProcessor == null) {
                        throw new IllegalStateException("Custom processor not specified");
                    }
                    if (customProcessor.getCacheType() != CacheType.CUSTOM) {
                        throw new IllegalStateException("Custom processor cache type not custom");
                    }
                    customCacheProcessors.add(customProcessor);
                    break;
                case JCACHE:
                case INFINISPAN:
                case HAZELCAST:
                case COUCHBASE:
                default:
                    throw new IllegalArgumentException("Cache type current not support: " + cacheType);
            }
        }

        processors = customCacheProcessors;
    }

    private boolean containsType(CacheType[] cacheTypes, CacheType cacheType) {
        for (CacheType type : cacheTypes) {
            if (type.equals(cacheType)) {
                return true;
            }
        }
        return false;
    }
}

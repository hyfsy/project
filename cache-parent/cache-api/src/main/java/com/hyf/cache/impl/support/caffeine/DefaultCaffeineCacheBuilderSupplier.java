package com.hyf.cache.impl.support.caffeine;

import java.util.concurrent.Executor;
import java.util.function.Supplier;

import javax.annotation.Resource;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import com.hyf.cache.impl.properties.CacheProperties;
import com.hyf.cache.impl.utils.ApplicationContextUtils;
import com.hyf.cache.impl.utils.StringUtils;
import com.github.benmanes.caffeine.cache.*;

/**
 * 默认caffeine缓存构建器生成
 * 
 * @author baB_hyf
 * @date 2022/02/17
 * @see Caffeine
 * @see com.hyf.cache.impl.properties.Caffeine
 */
public class DefaultCaffeineCacheBuilderSupplier implements CaffeineCacheBuilderSupplier, ApplicationContextAware
{

    public static final int UNSET_INT = -1;

    @Resource
    private CacheProperties cacheProperties;

    private ApplicationContext ctx;

    @Override
    public Caffeine<Object, Object> get(String cacheName) {

        Caffeine<Object, Object> caffeineBuilder = Caffeine.newBuilder();
        com.hyf.cache.impl.properties.Caffeine caffeine = cacheProperties.getCaffeine();

        buildExpireAfterAccess(caffeineBuilder, caffeine);
        buildRefreshAfterWrite(caffeineBuilder, caffeine);
        buildEvictionListener(caffeineBuilder, caffeine);
        buildRemovalListener(caffeineBuilder, caffeine);
        buildInitialCapacity(caffeineBuilder, caffeine);
        buildMaximumSize(caffeineBuilder, caffeine);
        buildRecordStats(caffeineBuilder, caffeine);
        buildReferenceKeysValues(caffeineBuilder, caffeine);
        buildScheduler(caffeineBuilder, caffeine);
        buildTicker(caffeineBuilder, caffeine);
        buildExecutor(caffeineBuilder, caffeine);

        return caffeineBuilder;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.ctx = applicationContext;
    }

    protected void buildExpireAfterAccess(Caffeine<Object, Object> caffeineBuilder, com.hyf.cache.impl.properties.Caffeine caffeine) {

        if (caffeine.getExpireAfterAccess() != null) {
            caffeineBuilder.expireAfterAccess(caffeine.getExpireAfterAccess());
        }
    }

    protected void buildRefreshAfterWrite(Caffeine<Object, Object> caffeineBuilder, com.hyf.cache.impl.properties.Caffeine caffeine) {

        if (caffeine.getRefreshAfterWrite() != null) {
            caffeineBuilder.refreshAfterWrite(caffeine.getRefreshAfterWrite());
        }

    }

    protected void buildEvictionListener(Caffeine<Object, Object> caffeineBuilder, com.hyf.cache.impl.properties.Caffeine caffeine) {

        if (StringUtils.isNotBlank(caffeine.getEvictionListener())) {
            caffeineBuilder.evictionListener(
                    ApplicationContextUtils.getBean(ctx, caffeine.getEvictionListener(), RemovalListener.class));
        }
    }

    protected void buildRemovalListener(Caffeine<Object, Object> caffeineBuilder, com.hyf.cache.impl.properties.Caffeine caffeine) {

        if (StringUtils.isNotBlank(caffeine.getRemovalListener())) {
            caffeineBuilder.removalListener(
                    ApplicationContextUtils.getBean(ctx, caffeine.getRemovalListener(), RemovalListener.class));
        }
    }

    protected void buildInitialCapacity(Caffeine<Object, Object> caffeineBuilder, com.hyf.cache.impl.properties.Caffeine caffeine) {

        if (caffeine.getInitialCapacity() != UNSET_INT) {
            caffeineBuilder.initialCapacity(caffeine.getInitialCapacity());
        }
    }

    protected void buildMaximumSize(Caffeine<Object, Object> caffeineBuilder, com.hyf.cache.impl.properties.Caffeine caffeine) {

        if (caffeine.getMaximumSize() != UNSET_INT) {
            caffeineBuilder.maximumSize(caffeine.getMaximumSize());
        }
        else {
            if (caffeine.getMaximumWeight() != UNSET_INT) {
                caffeineBuilder.maximumWeight(caffeine.getMaximumWeight());
            }
            if (StringUtils.isNotBlank(caffeine.getWeigher())) {
                caffeineBuilder.weigher(ApplicationContextUtils.getBean(ctx, caffeine.getWeigher(), Weigher.class));
            }
        }
    }

    protected void buildRecordStats(Caffeine<Object, Object> caffeineBuilder, com.hyf.cache.impl.properties.Caffeine caffeine) {

        if (!caffeine.isRecordStats()) {
            return;
        }

        if (StringUtils.isNotBlank(caffeine.getRecordStatsCounter())) {
            caffeineBuilder.recordStats(
                    ApplicationContextUtils.getBean(ctx, caffeine.getRecordStatsCounter(), Supplier.class));
        }
        else {
            caffeineBuilder.recordStats();
        }
    }

    protected void buildReferenceKeysValues(Caffeine<Object, Object> caffeineBuilder,
            com.hyf.cache.impl.properties.Caffeine caffeine) {

        if (caffeine.isWeakKeys()) {
            caffeineBuilder.weakKeys();
        }
        if (caffeine.isWeakValues()) {
            caffeineBuilder.weakValues();
        }
        else if (caffeine.isSoftValues()) {
            caffeineBuilder.softValues();
        }
    }

    protected void buildScheduler(Caffeine<Object, Object> caffeineBuilder, com.hyf.cache.impl.properties.Caffeine caffeine) {

        if (StringUtils.isNotBlank(caffeine.getScheduler())) {
            caffeineBuilder.scheduler(ApplicationContextUtils.getBean(ctx, caffeine.getScheduler(), Scheduler.class));
        }
    }

    protected void buildTicker(Caffeine<Object, Object> caffeineBuilder, com.hyf.cache.impl.properties.Caffeine caffeine) {

        if (StringUtils.isNotBlank(caffeine.getTicker())) {
            caffeineBuilder.ticker(ApplicationContextUtils.getBean(ctx, caffeine.getTicker(), Ticker.class));
        }
    }

    protected void buildExecutor(Caffeine<Object, Object> caffeineBuilder, com.hyf.cache.impl.properties.Caffeine caffeine) {

        if (StringUtils.isNotBlank(caffeine.getExecutor())) {
            caffeineBuilder.executor(ApplicationContextUtils.getBean(ctx, caffeine.getExecutor(), Executor.class));
        }
    }
}

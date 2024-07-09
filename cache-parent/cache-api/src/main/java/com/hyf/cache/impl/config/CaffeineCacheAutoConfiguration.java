package com.hyf.cache.impl.config;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Role;
import org.springframework.core.annotation.Order;

import com.hyf.cache.CacheManager;
import com.hyf.cache.CacheProcessConfigurer;
import com.hyf.cache.CacheProcessor;
import com.hyf.cache.enums.CacheType;
import com.hyf.cache.impl.process.CompositeCacheProcessor;
import com.hyf.cache.impl.support.MethodCacheProcessor;
import com.hyf.cache.impl.support.ReuseCacheProcessor;
import com.hyf.cache.impl.support.caffeine.AdaptiveCaffeineCacheManager;
import com.hyf.cache.impl.support.caffeine.DefaultCaffeineCacheBuilderSupplier;
import com.github.benmanes.caffeine.cache.CacheLoader;
import com.github.benmanes.caffeine.cache.Caffeine;

/**
 * caffeine缓存的自动配置
 * 
 * @author baB_hyf
 * @date 2022/02/17
 */
@ConditionalOnClass(Caffeine.class)
@ConditionalOnCacheEnabled(CacheType.CAFFEINE)
public class CaffeineCacheAutoConfiguration
{

    @Bean
    @ConditionalOnMissingBean(name = "adaptiveCaffeineCacheManager")
    public AdaptiveCaffeineCacheManager adaptiveCaffeineCacheManager(
            ObjectProvider<CacheLoader<Object, Object>> cacheLoaderObjectProvider) {
        AdaptiveCaffeineCacheManager adaptiveCaffeineCacheManager = new AdaptiveCaffeineCacheManager();
        cacheLoaderObjectProvider.ifUnique(adaptiveCaffeineCacheManager::setCacheLoader);
        return adaptiveCaffeineCacheManager;
    }

    @Bean
    @Order(-1000)
    @ConditionalOnMissingBean(name = "caffeineCacheProcessor")
    public CacheProcessor caffeineCacheProcessor(CacheProcessConfigurer caffeineCacheProcessConfigurer) {
        List<CacheProcessor> redisCacheProcessors = Arrays.asList(
                adaptiveCaffeineMethodCacheProcessor(caffeineCacheProcessConfigurer), //
                adaptiveCaffeineReuseCacheProcessor(caffeineCacheProcessConfigurer) //
        );
        return new CompositeCacheProcessor(redisCacheProcessors);
    }

    public CacheProcessor adaptiveCaffeineMethodCacheProcessor(CacheProcessConfigurer caffeineCacheProcessConfigurer) {
        MethodCacheProcessor methodCacheProcessor = new MethodCacheProcessor();
        methodCacheProcessor.setCacheProcessConfigurer(caffeineCacheProcessConfigurer);
        return methodCacheProcessor;
    }

    public CacheProcessor adaptiveCaffeineReuseCacheProcessor(CacheProcessConfigurer caffeineCacheProcessConfigurer) {
        ReuseCacheProcessor reuseCacheProcessor = new ReuseCacheProcessor();
        reuseCacheProcessor.setCacheProcessConfigurer(caffeineCacheProcessConfigurer);
        return reuseCacheProcessor;
    }

    @Bean
    @Role(BeanDefinition.ROLE_INFRASTRUCTURE)
    public CacheProcessConfigurer caffeineCacheProcessConfigurer(
            AdaptiveCaffeineCacheManager adaptiveCaffeineCacheManager) {
        return new CacheProcessConfigurer()
        {

            @Override
            public CacheManager getCacheManager() {
                return adaptiveCaffeineCacheManager;
            }
        };
    }

    @Bean
    @ConditionalOnMissingBean
    public DefaultCaffeineCacheBuilderSupplier defaultCaffeineCacheBuilderSupplier() {
        return new DefaultCaffeineCacheBuilderSupplier();
    }
}

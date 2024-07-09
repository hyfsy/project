package com.hyf.cache.impl.config;

import com.hyf.cache.impl.contact.redis.CacheRedisMsgReceiver;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

import com.hyf.cache.CacheMonitor;
import com.hyf.cache.impl.CacheMonitorImpl;
import com.hyf.cache.impl.cache.CacheEventPublisher;
import com.hyf.cache.impl.key.CacheKeyUtils;
import com.hyf.cache.impl.listener.CacheCoreListener;
import com.hyf.cache.impl.mode.CacheAsidePatternModeProcessor;
import com.hyf.cache.impl.mode.CustomCacheModeProcessor;
import com.hyf.cache.impl.mode.ReadThroughWriteThroughModeProcessor;
import com.hyf.cache.impl.mode.WriteBehindModeProcessor;
import com.hyf.cache.impl.process.CacheProcessChainFactoryBean;

/**
 * @author baB_hyf
 * @date 2022/03/05
 */
@Import(CacheBaseAutoConfiguration.CacheModeProcessorConfiguration.class)
public class CacheBaseAutoConfiguration
{

    @Bean
    public CacheProcessChainFactoryBean cacheProcessChainFactoryBean() {
        return new CacheProcessChainFactoryBean();
    }

    @Bean
    public CacheEventPublisher cacheEventPublisher() {
        return new CacheEventPublisher();
    }

    @Bean
    public CacheMonitor cacheMonitor() {
        return new CacheMonitorImpl();
    }

    @Bean
    public CacheKeyUtils cacheKeyUtils() {
        return new CacheKeyUtils();
    }

    @Bean
    @ConditionalOnBean(name = "cacheRedisTemplate")
    public CacheCoreListener cacheSyncListener() {
        return new CacheCoreListener();
    }

    @Bean
    @ConditionalOnBean(name = "cacheRedisTemplate")
    public CacheRedisMsgReceiver cacheRedisMsgReceiver() {
        return new CacheRedisMsgReceiver();
    }

    public static class CacheModeProcessorConfiguration
    {

        @Bean
        public CacheAsidePatternModeProcessor cacheAsidePatternModeProcessor() {
            return new CacheAsidePatternModeProcessor();
        }

        @Bean
        public ReadThroughWriteThroughModeProcessor readThroughWriteThroughModeProcessor() {
            return new ReadThroughWriteThroughModeProcessor();
        }

        @Bean
        public WriteBehindModeProcessor writeBehindModeProcessor() {
            return new WriteBehindModeProcessor();
        }

        @Bean
        public CustomCacheModeProcessor customCacheModeProcessor() {
            return new CustomCacheModeProcessor();
        }
    }
}

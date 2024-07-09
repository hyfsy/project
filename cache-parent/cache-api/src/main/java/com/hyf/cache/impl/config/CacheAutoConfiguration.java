package com.hyf.cache.impl.config;

import java.util.Arrays;

import org.aspectj.lang.reflect.Advice;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.couchbase.CouchbaseAutoConfiguration;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.autoconfigure.hazelcast.HazelcastAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.CacheManager;
import org.springframework.cache.interceptor.CacheInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.ImportSelector;
import org.springframework.core.type.AnnotationMetadata;

import com.hyf.cache.impl.properties.CacheProperties;

/**
 * 缓存自动配置主启动类
 * 
 * @author baB_hyf
 * @date 2022/02/22
 */
@Configuration
@ConditionalOnClass({Advice.class, CacheManager.class })
@ConditionalOnMissingBean(CacheInterceptor.class)
@ConditionalOnCacheEnabled
@EnableConfigurationProperties(CacheProperties.class)
@AutoConfigureAfter({CouchbaseAutoConfiguration.class, HazelcastAutoConfiguration.class,
        HibernateJpaAutoConfiguration.class, RedisAutoConfiguration.class })
@Import(CacheAutoConfiguration.CacheImportSelector.class)
public class CacheAutoConfiguration
{

    static class CacheImportSelector implements ImportSelector
    {

        @Override
        public String[] selectImports(AnnotationMetadata annotationMetadata) {

            Class<?>[] classes = new Class[] { //
                    CacheBaseAutoConfiguration.class, //
                    CacheAspectConfiguration.class, //
                    CacheKeyConfiguration.class, //
                    RedisCacheAutoConfiguration.class, //
                    CaffeineCacheAutoConfiguration.class, //
                    EhCacheAutoConfiguration.class, //
            };

            return Arrays.stream(classes).map(Class::getName).toArray(String[]::new);
        }
    }
}

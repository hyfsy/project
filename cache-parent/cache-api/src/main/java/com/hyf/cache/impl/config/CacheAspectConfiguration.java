package com.hyf.cache.impl.config;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.cache.annotation.AnnotationCacheOperationSource;
import org.springframework.cache.annotation.ProxyCachingConfiguration;
import org.springframework.cache.interceptor.BeanFactoryCacheOperationSourceAdvisor;
import org.springframework.cache.interceptor.CacheOperationSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Role;

import com.hyf.cache.impl.aspect.EpCacheInterceptor;
import com.hyf.cache.impl.operation.CustomCacheAnnotationParser;

/**
 * 缓存切面的自动配置
 *
 * @author baB_hyf
 * @date 2022/02/09
 */
@Role(BeanDefinition.ROLE_INFRASTRUCTURE)
@ConditionalOnMissingBean(ProxyCachingConfiguration.class)
public class CacheAspectConfiguration
{

    @Bean
    @Role(BeanDefinition.ROLE_INFRASTRUCTURE)
    public BeanFactoryCacheOperationSourceAdvisor epCacheAdvisor(CacheOperationSource epCacheOperationSource,
            EpCacheInterceptor epCacheInterceptor) {
        BeanFactoryCacheOperationSourceAdvisor advisor = new BeanFactoryCacheOperationSourceAdvisor();
        advisor.setCacheOperationSource(epCacheOperationSource);
        advisor.setAdvice(epCacheInterceptor);
        // not allow enable annotation
        // if (this.enableCaching != null) {
        // advisor.setOrder(this.enableCaching.<Integer>getNumber("order"));
        // }
        return advisor;
    }

    @Bean
    @Role(BeanDefinition.ROLE_INFRASTRUCTURE)
    public CacheOperationSource epCacheOperationSource() {
        // custom annotation
        return new AnnotationCacheOperationSource(new CustomCacheAnnotationParser());
    }

    @Bean
    @Role(BeanDefinition.ROLE_INFRASTRUCTURE)
    public EpCacheInterceptor epCacheInterceptor(CacheOperationSource epCacheOperationSource) {
        EpCacheInterceptor interceptor = new EpCacheInterceptor();
        interceptor.setCacheOperationSources(epCacheOperationSource);
        return interceptor;
    }
}

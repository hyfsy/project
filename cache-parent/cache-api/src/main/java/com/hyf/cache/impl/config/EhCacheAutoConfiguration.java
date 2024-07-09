package com.hyf.cache.impl.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;

import com.hyf.cache.enums.CacheType;

import net.sf.ehcache.Ehcache;

/**
 * TODO ehcache缓存的自动配置
 *
 * @author baB_hyf
 * @date 2022/02/22
 */
@ConditionalOnClass(Ehcache.class)
@ConditionalOnCacheEnabled(CacheType.EHCACHE)
public class EhCacheAutoConfiguration
{
}

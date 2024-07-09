package com.hyf.cache.impl;

import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;

import com.hyf.cache.CacheResolver;
import com.hyf.cache.KeyGenerator;
import com.hyf.cache.constants.CacheConstants;
import com.hyf.cache.enums.CacheMode;
import com.hyf.cache.enums.CacheType;

/**
 * 基于代码的缓存配置
 * 
 * @author baB_hyf
 * @date 2022/01/14
 * @see CacheTemplate
 */
public class CacheDefinition
{

    // TODO

    private String cacheName = "";
    private Method keyMapperMethod = null;
    private String keyMapperArgs = "#p0";
    private String keyType = "single";
    private KeyGenerator keyGenerator = null;

    // private CacheManager cacheManager = null;
    private CacheResolver cacheResolver = null;
    private boolean atomic = false;
    private boolean forceConsistency = false;

    private long ttl = CacheConstants.DEFAULT_TTL;
    private TimeUnit unit = CacheConstants.DEFAULT_TTL_UNIT;
    private boolean ttlRandom = false;
    // 方法返回为null是否进行缓存
    private boolean allowNull = true;

    private CacheMode cacheMode = CacheMode.CACHE_ASIDE_PATTERN;
    private CacheType cacheType = null;
}

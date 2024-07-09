package com.hyf.cache.constants;

import java.util.concurrent.TimeUnit;

import com.hyf.cache.enums.CacheMode;

/**
 * 缓存常量
 * 
 * @author baB_hyf
 * @date 2022/01/14
 */
public class CacheConstants
{

    /**
     * 缓存配置前缀
     */
    public static final String CACHE_PROPERTY_PREFIX = "spring.hyf.cache";

    /**
     * 默认缓存配置
     */
    public static final long DEFAULT_TTL = 30;

    public static final TimeUnit DEFAULT_TTL_UNIT = TimeUnit.MINUTES;

    public static final CacheMode DEFAULT_CACHE_MODE = CacheMode.CACHE_ASIDE_PATTERN;

    public static final boolean FORCE_CONSISTENCY = false;
}

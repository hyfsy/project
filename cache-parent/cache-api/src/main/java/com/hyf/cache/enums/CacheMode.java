package com.hyf.cache.enums;

/**
 * 缓存执行模式
 *
 * @author baB_hyf
 * @date 2022/01/14
 */
public enum CacheMode
{

    CACHE_ASIDE_PATTERN, //
    READ_THROUGH_WRITE_THROUGH, //
    WRITE_BEHIND, //
    // 支持自定义执行模式
    CUSTOM, //
    ;
}

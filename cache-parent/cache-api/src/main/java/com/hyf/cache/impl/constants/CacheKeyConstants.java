package com.hyf.cache.impl.constants;

/**
 * 缓存key相关的常量
 * 
 * @author baB_hyf
 * @date 2022/02/08
 */
public class CacheKeyConstants
{

    /**
     * 缓存key版本
     */
    public static final String VERSION = "cache.v1";

    /**
     * 缓存key分隔符
     */
    public static final String KEY_SEPARATOR = ":";
    public static final String KEY_SEGMENT_SEPARATOR = ".";

    /**
     * 缓存全局锁名称后缀
     */
    public static final String LOCK_SUFFIX = "~lock";

    /**
     * spring注解生成的缓存key类型
     */
    public static final String TYPE_SPRING = "spring";

    /**
     * 自定义缓存key类型
     */
    public static final String TYPE_CUSTOM = "custom";

    /**
     * 业务缓存key类型
     */
    public static final String TYPE_BUSINESS = "business";
    public static final String TYPE_BUSINESS_REUSE = "reuse";
    public static final String TYPE_BUSINESS_INDEX = "index";
    public static final String TYPE_BUSINESS_METHOD = "method";

    /**
     * 全局缓存key的索引key类型
     */
    public static final String TYPE_INDEX = "index";
    public static final String TYPE_INDEX_SPRING = "index_spring";
    public static final String TYPE_INDEX_CUSTOM = "index_custom";
    public static final String TYPE_INDEX_BUSINESS = "index_business";
    public static final String TYPE_INDEX_BUSINESS_REUSE = "index_business_reuse";
    public static final String TYPE_INDEX_BUSINESS_INDEX = "index_business_index";
    public static final String TYPE_INDEX_BUSINESS_METHOD = "index_business_method";

}

package com.hyf.cache.enums;

/**
 * 缓存类型
 *
 * @author baB_hyf
 * @date 2022/01/14
 */
public enum CacheType
{

    GENERIC(CacheStructType.DISTRIBUTED), //
    JCACHE(CacheStructType.DISTRIBUTED), //
    HAZELCAST(CacheStructType.DISTRIBUTED), //
    INFINISPAN(CacheStructType.DISTRIBUTED), //
    COUCHBASE(CacheStructType.DISTRIBUTED), //

    // 暂时仅支持下面的

    REDIS(CacheStructType.DISTRIBUTED), //
    CAFFEINE(CacheStructType.LOCAL), //
    EHCACHE(CacheStructType.LOCAL), //
    SIMPLE(CacheStructType.DISTRIBUTED), //
    CUSTOM(CacheStructType.DISTRIBUTED), //
    NONE(CacheStructType.DISTRIBUTED), //
    ;

    CacheStructType cacheStructType;

    CacheType(CacheStructType cacheStructType) {
        this.cacheStructType = cacheStructType;
    }

    public boolean isLocalCache() {
        return cacheStructType == CacheStructType.LOCAL;
    }
}

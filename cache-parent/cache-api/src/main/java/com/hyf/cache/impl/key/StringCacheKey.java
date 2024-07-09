package com.hyf.cache.impl.key;

import com.hyf.cache.CacheKey;
import com.hyf.cache.impl.constants.CacheKeyConstants;

/**
 * 基于string的缓存key
 * 
 * @author baB_hyf
 * @date 2022/02/18
 */
public class StringCacheKey implements CacheKey
{

    private final String version;
    private final String type;
    private final String application;
    private final String tenant;
    private final String cacheName;
    private final String key;

    public StringCacheKey(String key) {
        String[] sps = key.split(CacheKeyConstants.KEY_SEPARATOR);
        if (sps.length < 5) {
            throw new IllegalArgumentException(key);
        }
        this.version = sps[0];
        this.type = sps[1];
        this.application = sps[2];
        this.tenant = sps[3];
        this.cacheName = sps[4];
        this.key = key;
    }

    @Override
    public String toString() {
        return key;
    }

    @Override
    public String getType() {
        return type;
    }

    @Override
    public String getVersion() {
        return version;
    }

    @Override
    public String getApplication() {
        return application;
    }

    @Override
    public String getTenant() {
        return tenant;
    }

    @Override
    public String getCacheName() {
        return cacheName;
    }

}

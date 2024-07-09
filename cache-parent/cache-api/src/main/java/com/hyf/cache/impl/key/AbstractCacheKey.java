package com.hyf.cache.impl.key;

import com.hyf.cache.CacheKey;
import com.hyf.cache.impl.constants.CacheKeyConstants;

/**
 * 基于默认前缀的缓存key
 *
 * @author baB_hyf
 * @date 2022/02/08
 */
public abstract class AbstractCacheKey implements CacheKey
{

    private final String version;
    private final String application;
    private final String tenant;
    private final String cacheName;

    public AbstractCacheKey(String cacheName) {
        this.version = CacheKeyUtils.getVersion();
        this.application = CacheKeyUtils.getApplication();
        this.tenant = CacheKeyUtils.getTenant();
        this.cacheName = cacheName;
    }

    public AbstractCacheKey(String version, String application, String tenant, String cacheName) {
        this.version = version;
        this.application = application;
        this.tenant = tenant;
        this.cacheName = cacheName;
    }

    @Override
    public final String toString() {
        return String.join(CacheKeyConstants.KEY_SEPARATOR, version, getType(), application, tenant, cacheName,
                custom());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        AbstractCacheKey that = (AbstractCacheKey) o;
        return this.toString().equals(that.toString());
    }

    @Override
    public final int hashCode() {
        return this.toString().hashCode();
    }

    protected abstract String custom();

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

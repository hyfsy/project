package com.hyf.cache.impl.key;

/**
 * @author baB_hyf
 * @date 2022/02/11
 */
public class BusinessIndexCacheKey extends IndexCacheKey
{

    private final String cacheClassName;
    private final String businessType;

    public BusinessIndexCacheKey(String cacheName, String type, String cacheClassName, String businessType) {
        super(cacheName, type);
        this.cacheClassName = cacheClassName;
        this.businessType = businessType;
    }

    @Override
    protected String custom() {
        return String.join(":", super.custom(), getCacheClassName(), getBusinessType());
    }

    public String getCacheClassName() {
        return cacheClassName;
    }

    public String getBusinessType() {
        return businessType;
    }
}

package com.hyf.cache.impl.key;

import com.hyf.cache.impl.constants.CacheKeyConstants;

/**
 * 业务相关key
 * 
 * @author baB_hyf
 * @date 2022/02/08
 */
public abstract class AbstractBusinessCacheKey extends AbstractCacheKey
{

    private final String businessClassName;
    private final String businessType;

    public AbstractBusinessCacheKey(String cacheName, String businessClassName, String businessType) {
        super(cacheName);
        this.businessClassName = businessClassName;
        this.businessType = businessType;
    }

    public String getBusinessClassName() {
        return businessClassName;
    }

    public String getBusinessType() {
        return businessType;
    }

    @Override
    protected String custom() {
        return String.join(":", CacheKeyConstants.TYPE_BUSINESS, getBusinessClassName(), getBusinessType(),
                businessSegment());
    }

    protected abstract String businessSegment();

    @Override
    public String getType() {
        return getBusinessType();
    }
}

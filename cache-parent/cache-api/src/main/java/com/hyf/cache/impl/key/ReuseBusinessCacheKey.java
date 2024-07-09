package com.hyf.cache.impl.key;

import com.hyf.cache.impl.constants.CacheKeyConstants;

/**
 * @author baB_hyf
 * @date 2022/02/08
 */
public class ReuseBusinessCacheKey extends AbstractBusinessCacheKey {

    private final String pk;

    public ReuseBusinessCacheKey(String cacheName, String businessClassName, String pk) {
        super(cacheName, businessClassName, CacheKeyConstants.TYPE_BUSINESS_REUSE);
        this.pk = pk;
    }

    public String getPk() {
        return pk;
    }

    @Override
    protected String businessSegment() {
        return pk;
    }
}

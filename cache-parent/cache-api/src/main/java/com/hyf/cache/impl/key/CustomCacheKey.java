package com.hyf.cache.impl.key;

import com.hyf.cache.impl.constants.CacheKeyConstants;

/**
 * @author baB_hyf
 * @date 2022/02/08
 */
public class CustomCacheKey extends AbstractCacheKey
{

    private final String custom;

    public CustomCacheKey(String cacheName, String custom) {
        super(cacheName);
        this.custom = custom;
    }

    public String getCustom() {
        return custom;
    }

    @Override
    protected String custom() {
        return getCustom();
    }

    @Override
    public String getType() {
        return CacheKeyConstants.TYPE_CUSTOM;
    }
}

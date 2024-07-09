package com.hyf.cache.impl.key;

import com.hyf.cache.impl.constants.CacheKeyConstants;

/**
 * 全局索引缓存key
 * 
 * @author baB_hyf
 * @date 2022/02/08
 */
public class IndexCacheKey extends AbstractCacheKey
{

    private final String type;

    public IndexCacheKey(String cacheName, String type) {
        super(cacheName);
        this.type = type;
    }

    @Override
    protected String custom() {
        return String.join(":", CacheKeyConstants.TYPE_INDEX, getType());
    }

    @Override
    public String getType() {
        return this.type;
    }
}

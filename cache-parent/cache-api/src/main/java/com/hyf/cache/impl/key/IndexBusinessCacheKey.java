package com.hyf.cache.impl.key;

import com.hyf.cache.impl.constants.CacheKeyConstants;

/**
 * @author baB_hyf
 * @date 2022/02/08
 */
public class IndexBusinessCacheKey extends AbstractBusinessCacheKey {

    private final String fieldName;
    private final String fieldValue;
    private final String guid;

    public IndexBusinessCacheKey(String cacheName, String businessClassName, String fieldName,String fieldValue,String guid) {
        super(cacheName, businessClassName, CacheKeyConstants.TYPE_BUSINESS_INDEX);
        this.fieldName = fieldName;
        this.fieldValue = fieldValue;
        this.guid = guid;
    }

    public String getFieldName() {
        return fieldName;
    }

    public String getFieldValue() {
        return fieldValue;
    }

    public String getGuid() {
        return guid;
    }

    @Override
    protected String businessSegment() {
        return String.join(".", getFieldName(), getFieldValue(), getGuid());
    }
}

package com.hyf.cache.impl;

import com.hyf.cache.CacheKey;
import com.hyf.cache.impl.constants.CacheKeyConstants;
import com.hyf.cache.impl.key.MethodBusinessCacheKey;

/**
 * 解析缓存key
 * 
 * @author baB_hyf
 * @date 2022/01/18
 * @see CacheKey
 */
public class CacheKeyParser
{

    // TODO 提供校验

    public static <T extends CacheKey> T parse(Object key, Class<T> clazz) {
        CacheKey k = parse(key);
        return clazz.cast(k);
    }

    public static CacheKey parse(Object key) {
        // TODO
        if (!(key instanceof String)) {
            throw new UnsupportedOperationException();
        }

        String keyString = (String) key;
        String[] keySegment = keyString.split(":");
        String version = keySegment[0];
        String type = keySegment[1];
        String app = keySegment[2];
        String tenant = keySegment[3];
        String cacheName = keySegment[4];

        switch (type) {
            // case CacheKeyConstants.TYPE_BUSINESS_REUSE:
            // break;
            // case CacheKeyConstants.TYPE_BUSINESS_INDEX:
            // break;
            case CacheKeyConstants.TYPE_BUSINESS_METHOD:
                String business = keySegment[5];
                String businessClassName = keySegment[6];
                String method = keySegment[7];
                String customId = keySegment[8];

                // String argsString = keySegment[9];

                // CMA cma = CMA.parse(argsString);
                return new MethodBusinessCacheKey(cacheName, businessClassName, customId);
            default:
                throw new IllegalArgumentException(keyString);
        }
    }
}

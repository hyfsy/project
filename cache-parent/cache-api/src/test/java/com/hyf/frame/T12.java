package com.hyf.frame;

import com.hyf.cache.CacheKey;
import com.hyf.cache.impl.key.generator.CacheKeyBuilder2;

/**
 * @author baB_hyf
 * @date 2022/02/21
 */
public class T12
{

    public static void main(String[] args) {
        CacheKey k = CacheKeyBuilder2.business("user").cacheClassName("getCacheClassName").method().signature("*")
                .build();
        System.out.println(k);
        CacheKey key = CacheKeyBuilder2.newBuilder("user").type("*").suffix("*").build();
        System.out.println(key);
    }
}

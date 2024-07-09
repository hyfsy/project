package com.hyf.frame;

import com.hyf.cache.CacheKey;
import com.hyf.cache.impl.key.CMA;
import com.hyf.cache.impl.key.generator.CacheKeyBuilder;

/**
 * @author baB_hyf
 * @date 2022/02/18
 */
public class T9
{

    public static void main(String[] args) {
        String cacheName = "cacheName";
        String cacheClassName = "businessClassName";

        System.out.println(CacheKeyBuilder.newBuilder(cacheName).custom("customString"));

        String signature = CMA.toString(CacheKeyBuilder.class.getName(), "methodName", null);
        System.out.println(CacheKeyBuilder.newBuilder(cacheName).spring(signature));

        CacheKey cacheKey = CacheKeyBuilder.newBuilder(cacheName)
                .business(cacheClassName)
                .method()
                .guid("guid")
                .signature("signature")
                .build();
        System.out.println(cacheKey);

        System.out.println(CacheKeyBuilder.newBuilder(cacheName).business(cacheClassName).reuse().guid("guid").build());

        System.out.println(CacheKeyBuilder.newBuilder(cacheName).business(cacheClassName).reuseIndex()
                .fieldName("field_name").fieldValue("field_value").guid("guid").build());

        System.out.println(CacheKeyBuilder.newBuilder(cacheName).index().customIndex());
        System.out.println(CacheKeyBuilder.newBuilder(cacheName).index().springIndex());
        System.out.println(CacheKeyBuilder.newBuilder(cacheName).index().businessIndexIndex(cacheClassName));
        System.out.println(CacheKeyBuilder.newBuilder(cacheName).index().businessReuseIndex(cacheClassName));
        System.out.println(CacheKeyBuilder.newBuilder(cacheName).index().businessMethodIndex(cacheClassName));


        System.out.println("=======================================");

        System.out.println(CacheKeyBuilder.newBuilder(cacheName).suffix("*").build());
        System.out.println(CacheKeyBuilder.newBuilder(cacheName).suffix("*").spring(""));
        System.out.println(CacheKeyBuilder.newBuilder(cacheName).suffix(":*").custom(""));
        System.out.println(CacheKeyBuilder.newBuilder(cacheName).business("").suffix("*").build());
        System.out.println(CacheKeyBuilder.newBuilder(cacheName).business("").reuse().suffix("*").build());
        System.out.println(CacheKeyBuilder.newBuilder(cacheName).business("").reuse().guid("").suffix("*").build());
        System.out.println(CacheKeyBuilder.newBuilder(cacheName).business("").method().guid("").signature(signature).suffix("*").build());
        System.out.println(CacheKeyBuilder.newBuilder(cacheName).business("").method().guid("guid").signature("").suffix("*").build());

        System.out.println(CacheKeyBuilder.newBuilder(cacheName).index().suffix("*").build());
        System.out.println(CacheKeyBuilder.newBuilder(cacheName).index().suffix("*").build());
        System.out.println(CacheKeyBuilder.newBuilder(cacheName).index().suffix("*").spring(""));
        System.out.println(CacheKeyBuilder.newBuilder(cacheName).index().suffix("*").custom(""));
        System.out.println(CacheKeyBuilder.newBuilder(cacheName).suffix("*").index().businessIndexIndex(""));
        System.out.println(CacheKeyBuilder.newBuilder(cacheName).suffix("*").index().businessMethodIndex(""));
        System.out.println(CacheKeyBuilder.newBuilder(cacheName).suffix("*").index().businessReuseIndex(""));


        System.out.println(CacheKeyBuilder.newBuilder(cacheName).business("*").reuse().suffix("*").build());

        System.out.println(CacheKeyBuilder.newBuilder(cacheName).version("version").app("app").tenant("tenant").suffix("*").spring(signature));

    }
}

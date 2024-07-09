package com.hyf.frame;

import com.hyf.cache.impl.key.generator.CacheKeyBuilder2;

/**
 * @author baB_hyf
 * @date 2022/02/19
 */
public class T11
{

    public static void main(String[] args) {
        System.out.println(CacheKeyBuilder2.newBuilder("").app("app").version("version").tenant("tenant").type("type").build());

        System.out.println(CacheKeyBuilder2.index("cacheName").spring().build());
        System.out.println(CacheKeyBuilder2.index("cacheName").custom().build());
        System.out.println(CacheKeyBuilder2.index("cacheName").business().build());
        System.out.println(CacheKeyBuilder2.index("cacheName").business().reuse().build());
        System.out.println(CacheKeyBuilder2.index("cacheName").business().index().build());
        System.out.println(CacheKeyBuilder2.index("cacheName").business().method().build());
        System.out.println(CacheKeyBuilder2.index("cacheName").business().reuse().cacheClassName("cacheClassName").build());
        System.out.println(CacheKeyBuilder2.index("cacheName").business().index().cacheClassName("cacheClassName").build());
        System.out.println(CacheKeyBuilder2.index("cacheName").business().method().cacheClassName("cacheClassName").build());
        System.out.println(CacheKeyBuilder2.index("cacheName").business().cacheClassName("cacheClassName").reuse().build());
        System.out.println(CacheKeyBuilder2.index("cacheName").business().cacheClassName("cacheClassName").index().build());
        System.out.println(CacheKeyBuilder2.index("cacheName").business().cacheClassName("cacheClassName").method().build());

        System.out.println(CacheKeyBuilder2.index("cacheName").spring().suffix("*").build());
        System.out.println(CacheKeyBuilder2.index("cacheName").custom().suffix("*").build());
        System.out.println(CacheKeyBuilder2.index("cacheName").business().suffix("*").build());
        System.out.println(CacheKeyBuilder2.index("cacheName").business().reuse().suffix("*").build());
        System.out.println(CacheKeyBuilder2.index("cacheName").business().index().suffix("*").build());
        System.out.println(CacheKeyBuilder2.index("cacheName").business().method().suffix("*").build());
        System.out.println(CacheKeyBuilder2.index("cacheName").business().reuse().cacheClassName("cacheClassName").suffix("*").build());
        System.out.println(CacheKeyBuilder2.index("cacheName").business().index().cacheClassName("cacheClassName").suffix("*").build());
        System.out.println(CacheKeyBuilder2.index("cacheName").business().method().cacheClassName("cacheClassName").suffix("*").build());
        System.out.println(CacheKeyBuilder2.index("cacheName").business().cacheClassName("cacheClassName").reuse().suffix("*").build());
        System.out.println(CacheKeyBuilder2.index("cacheName").business().cacheClassName("cacheClassName").index().suffix("*").build());
        System.out.println(CacheKeyBuilder2.index("cacheName").business().cacheClassName("cacheClassName").method().suffix("*").build());

        System.out.println(CacheKeyBuilder2.spring("cacheName").signature("").build());
        System.out.println(CacheKeyBuilder2.spring("cacheName").signature("signature").build());
        System.out.println(CacheKeyBuilder2.custom("cacheName").customString("").build());
        System.out.println(CacheKeyBuilder2.custom("cacheName").customString("customString").build());
        System.out.println(CacheKeyBuilder2.business("cacheName").cacheClassName("").build());
        System.out.println(CacheKeyBuilder2.business("cacheName").cacheClassName("").reuse().build());
        System.out.println(CacheKeyBuilder2.business("cacheName").cacheClassName("").reuse().guid("guid").build());
        System.out.println(CacheKeyBuilder2.business("cacheName").cacheClassName("").index().fieldName("").fieldValue("").guid("").build());
        System.out.println(CacheKeyBuilder2.business("cacheName").cacheClassName("").index().fieldName("fieldName").fieldValue("").guid("").build());
        System.out.println(CacheKeyBuilder2.business("cacheName").cacheClassName("").index().fieldName("").fieldValue("fieldValue").guid("").build());
        System.out.println(CacheKeyBuilder2.business("cacheName").cacheClassName("").index().fieldName("fieldName").fieldValue("fieldValue").guid("guid").build());
        System.out.println(CacheKeyBuilder2.business("cacheName").cacheClassName("").method().signature("").build());
        System.out.println(CacheKeyBuilder2.business("cacheName").cacheClassName("").method().signature("signature").build());
        System.out.println(CacheKeyBuilder2.business("cacheName").cacheClassName("cacheClassName").build());
        System.out.println(CacheKeyBuilder2.business("cacheName").cacheClassName("cacheClassName").reuse().build());
        System.out.println(CacheKeyBuilder2.business("cacheName").cacheClassName("cacheClassName").reuse().guid("guid").build());
        System.out.println(CacheKeyBuilder2.business("cacheName").cacheClassName("cacheClassName").index().fieldName("").fieldValue("").guid("").build());
        System.out.println(CacheKeyBuilder2.business("cacheName").cacheClassName("cacheClassName").index().fieldName("fieldName").fieldValue("").guid("").build());
        System.out.println(CacheKeyBuilder2.business("cacheName").cacheClassName("cacheClassName").index().fieldName("").fieldValue("fieldValue").guid("").build());
        System.out.println(CacheKeyBuilder2.business("cacheName").cacheClassName("cacheClassName").index().fieldName("fieldName").fieldValue("fieldValue").guid("guid").build());
        System.out.println(CacheKeyBuilder2.business("cacheName").cacheClassName("cacheClassName").method().signature("").build());
        System.out.println(CacheKeyBuilder2.business("cacheName").cacheClassName("cacheClassName").method().signature("signature").build());

        System.out.println(CacheKeyBuilder2.spring("cacheName").signature("").suffix("*").build());
        System.out.println(CacheKeyBuilder2.spring("cacheName").signature("signature").suffix("*").build());
        System.out.println(CacheKeyBuilder2.custom("cacheName").customString("").suffix("*").build());
        System.out.println(CacheKeyBuilder2.custom("cacheName").customString("customString").suffix("*").build());
        System.out.println(CacheKeyBuilder2.business("cacheName").cacheClassName("").suffix("*").build());
        System.out.println(CacheKeyBuilder2.business("cacheName").cacheClassName("").reuse().suffix("*").build());
        System.out.println(CacheKeyBuilder2.business("cacheName").cacheClassName("").reuse().guid("guid").suffix("*").build());
        System.out.println(CacheKeyBuilder2.business("cacheName").cacheClassName("").index().fieldName("").fieldValue("").guid("").suffix("*").build());
        System.out.println(CacheKeyBuilder2.business("cacheName").cacheClassName("").index().fieldName("fieldName").fieldValue("").guid("").suffix("*").build());
        System.out.println(CacheKeyBuilder2.business("cacheName").cacheClassName("").index().fieldName("").fieldValue("fieldValue").guid("").suffix("*").build());
        System.out.println(CacheKeyBuilder2.business("cacheName").cacheClassName("").index().fieldName("fieldName").fieldValue("fieldValue").guid("guid").suffix("*").build());
        System.out.println(CacheKeyBuilder2.business("cacheName").cacheClassName("").method().signature("").suffix("*").build());
        System.out.println(CacheKeyBuilder2.business("cacheName").cacheClassName("").method().signature("signature").suffix("*").build());
        System.out.println(CacheKeyBuilder2.business("cacheName").cacheClassName("cacheClassName").suffix("*").build());
        System.out.println(CacheKeyBuilder2.business("cacheName").cacheClassName("cacheClassName").reuse().suffix("*").build());
        System.out.println(CacheKeyBuilder2.business("cacheName").cacheClassName("cacheClassName").reuse().guid("guid").suffix("*").build());
        System.out.println(CacheKeyBuilder2.business("cacheName").cacheClassName("cacheClassName").index().fieldName("").fieldValue("").guid("").suffix("*").build());
        System.out.println(CacheKeyBuilder2.business("cacheName").cacheClassName("cacheClassName").index().fieldName("fieldName").fieldValue("").guid("").suffix("*").build());
        System.out.println(CacheKeyBuilder2.business("cacheName").cacheClassName("cacheClassName").index().fieldName("").fieldValue("fieldValue").guid("").suffix("*").build());
        System.out.println(CacheKeyBuilder2.business("cacheName").cacheClassName("cacheClassName").index().fieldName("fieldName").fieldValue("fieldValue").guid("guid").suffix("*").build());
        System.out.println(CacheKeyBuilder2.business("cacheName").cacheClassName("cacheClassName").method().signature("").suffix("*").build());
        System.out.println(CacheKeyBuilder2.business("cacheName").cacheClassName("cacheClassName").method().signature("signature").suffix("*").build());
    }
}

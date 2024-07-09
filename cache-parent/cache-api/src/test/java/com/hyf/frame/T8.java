package com.hyf.frame;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;

/**
 * @author baB_hyf
 * @date 2022/02/18
 */
public class T8 {

    public static void main(String[] args) throws InterruptedException {
        Cache<Object, Object> cache = Caffeine.newBuilder()
                .build();
        cache.put("1", "111");

        Thread.sleep(1000L);

        System.out.println(cache.getIfPresent("1"));

    }
}

package com.hyf.cache.custom.caffeine;

import org.checkerframework.checker.index.qual.NonNegative;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.springframework.stereotype.Component;

import com.github.benmanes.caffeine.cache.Weigher;

/**
 * @author baB_hyf
 * @date 2022/02/17
 */
@Component("CaffeineWeigher")
public class CaffeineWeigher implements Weigher<Object, Object>
{

    @Override
    public @NonNegative int weigh(@NonNull Object key, @NonNull Object value) {
        System.out.println("CaffeineWeigher");
        return 100;
    }
}

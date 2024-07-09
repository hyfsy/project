package com.hyf.cache.custom.caffeine;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.springframework.stereotype.Component;

import com.github.benmanes.caffeine.cache.CacheLoader;

/**
 * @author baB_hyf
 * @date 2022/02/24
 */
@Component
public class CaffeineCacheLoader implements CacheLoader<Object, Object>
{
    @Override
    public @Nullable Object load(@NonNull Object key) throws Exception {
        return null;
    }
}

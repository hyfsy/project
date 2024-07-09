package com.hyf.cache.custom.caffeine;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.springframework.stereotype.Component;

import com.github.benmanes.caffeine.cache.RemovalCause;
import com.github.benmanes.caffeine.cache.RemovalListener;

/**
 * @author baB_hyf
 * @date 2022/02/17
 */
@Component("CaffeineEvicationListener")
public class CaffeineEvicationListener implements RemovalListener<Object, Object>
{

    @Override
    public void onRemoval(@Nullable Object key, @Nullable Object value, @NonNull RemovalCause cause) {
        System.out.println("CaffeineEvicationListener");
    }
}

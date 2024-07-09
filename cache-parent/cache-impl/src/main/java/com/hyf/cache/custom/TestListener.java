package com.hyf.cache.custom;

import org.springframework.stereotype.Component;

import com.hyf.cache.CacheEvent;
import com.hyf.cache.CacheListener;
import com.hyf.cache.enums.CacheType;

/**
 * @author baB_hyf
 * @date 2022/02/15
 */
@Component
public class TestListener implements CacheListener
{

    @Override
    public boolean interest(CacheType cacheType, String cacheName, Object cacheKey) {

        return true;
    }

    @Override
    public void onHit(CacheEvent event) {
        print(event);
    }

    @Override
    public void onCreated(CacheEvent event) {
        print(event);
    }

    @Override
    public void onModified(CacheEvent event) {
        print(event);
    }

    @Override
    public void onEvicted(CacheEvent event) {
        print(event);
    }

    @Override
    public void onExpired(CacheEvent event) {
        print(event);
    }

    public void print(CacheEvent event) {
        System.out.println("自定义缓存监听器testListener输出事件信息：||" + event);
    }
}

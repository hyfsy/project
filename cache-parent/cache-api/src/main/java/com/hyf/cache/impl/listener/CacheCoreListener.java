package com.hyf.cache.impl.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.hyf.cache.CacheEvent;
import com.hyf.cache.CacheListener;
import com.hyf.cache.enums.CacheType;
import com.hyf.cache.impl.contact.ICacheContacter;

@Component
public class CacheCoreListener implements CacheListener
{
    private static final Logger logger = LoggerFactory.getLogger(CacheCoreListener.class);

    @Autowired
    private ICacheContacter cacheContacter;

    /**
     * 控制是否需要关注这个监听器，内置监听器默认true
     */
    @Override
    public boolean interest(CacheType cacheType, String cacheName, Object cacheKey) {
        return true;
    }

    @Override
    public void onEvicted(CacheEvent event) {
        logger.info("{}监听到缓存驱逐（evicted）事件{}", event.getCacheType(), event);
        cacheContacter.askSync(event);
    }

    /**
     * 当是caffeine缓存且是集群或者微服务模式时，需要判断是否需要广播修改信息
     */
    @Override
    public void onModified(CacheEvent event) {
        logger.info("{}监听到缓存修改（modified）事件{}", event.getCacheType(), event);
    }

    @Override
    public void onHit(CacheEvent event) {
        logger.info("{}监听到缓存击中（hit）事件{}", event.getCacheType(), event);
    }

    @Override
    public void onCreated(CacheEvent event) {
        logger.info("{}监听到缓存创建（create）事件{}", event.getCacheType(), event);
    }

    @Override
    public void onExpired(CacheEvent event) {
        logger.info("{}监听到缓存过期（expire）事件{}", event.getCacheType(), event);
    }

}

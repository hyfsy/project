package com.hyf.cache.impl.support.redis;

import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.listener.KeyExpirationEventMessageListener;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;

import com.hyf.cache.CacheKey;
import com.hyf.cache.enums.CacheEventType;
import com.hyf.cache.enums.CacheType;
import com.hyf.cache.impl.CacheKeyParser;
import com.hyf.cache.impl.cache.CacheEventPublisher;

/**
 * redis的expire key事件的监听器
 *
 * @author baB_hyf
 * @date 2022/02/15
 */
public class RedisCacheKeyExpirationEventMessageListener extends KeyExpirationEventMessageListener
{

    public RedisCacheKeyExpirationEventMessageListener(RedisMessageListenerContainer listenerContainer) {
        super(listenerContainer);
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        CacheKey key = CacheKeyParser.parse(new String(message.getBody()));
        CacheEventPublisher.publishEvent(CacheEventType.EXPIRE, key.getCacheName(), CacheType.REDIS, key, null, null);
    }
}

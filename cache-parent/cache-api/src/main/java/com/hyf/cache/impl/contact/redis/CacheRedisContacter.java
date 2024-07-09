package com.hyf.cache.impl.contact.redis;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import com.hyf.cache.CacheEvent;
import com.hyf.cache.impl.contact.CacheContactConstValue;
import com.hyf.cache.impl.contact.CacheContactMsg;
import com.hyf.cache.impl.contact.ICacheContacter;

@Component
public class CacheRedisContacter implements ICacheContacter
{

    private static final Logger logger = LoggerFactory.getLogger(CacheRedisMsgReceiver.class);

    @Resource(name = "cacheRedisTemplate")
    private RedisTemplate<String, String> redisTemplate;

    @Override
    public void askSync(CacheEvent event) {
        if (event.getCacheType().isLocalCache()) {
            CacheContactMsg msg = new CacheContactMsg(CacheContactMsg.MsgType.DELETE, event.getKey().toString(), event.getCacheName());
            logger.info("广播了信息{}", msg);
            redisTemplate.convertAndSend(CacheContactConstValue.REDIS_CONTACT_TOPIC, msg);
        }
    }

}

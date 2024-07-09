package com.hyf.cache.impl.contact.redis;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;

import com.hyf.cache.CacheKey;
import com.hyf.cache.impl.CacheKeyParser;
import com.hyf.cache.impl.config.RedisCacheAutoConfiguration;
import com.hyf.cache.impl.contact.CacheContactConstValue;
import com.hyf.cache.impl.contact.CacheContactMsg;
import com.hyf.cache.impl.support.caffeine.AdaptiveCaffeineCacheManager;

public class CacheRedisMsgReceiver
{

    private static final Logger logger = LoggerFactory.getLogger(CacheRedisMsgReceiver.class);

    @Resource(name = "cacheRedisTemplate")
    private RedisTemplate<String, String> redisTemplate;

    @Resource(name = "adaptiveCaffeineCacheManager")
    private AdaptiveCaffeineCacheManager adaptiveCaffeineCacheManager;

    public void getMessage(String content) {
        Jackson2JsonRedisSerializer<Object> seria = RedisCacheAutoConfiguration.defaultSerializer();
        CacheContactMsg msg = (CacheContactMsg) seria.deserialize(content.getBytes());

        // 这边需要判断是否接收的消息是自己发出去的，如果是则跳过
        if (CacheContactConstValue.REDIS_CLIENT_ID.equals(msg.getClientId())) {
            logger.info("自己广播的消息不做处理");
            return;
        }

        CacheKey cacheKey = CacheKeyParser.parse(msg.getCacheKey());

        // TODO 判断是不是redis集群或者微服务集群

        // 修改/删除操作 （如果不是guid） 删除所有实体类下的

        // cacheProcessor.
        // 防止无限广播删除
        adaptiveCaffeineCacheManager.findCacheNotListener(msg.getCacheName()).evict(cacheKey);
        logger.info("内置redis话题收到消息 准备删除caffeine对应缓存：{}" + content);
    }
}

package com.hyf.cache.impl.config;

import java.util.Arrays;
import java.util.List;

import org.redisson.Redisson;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Role;
import org.springframework.core.annotation.Order;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;

import com.hyf.cache.CacheManager;
import com.hyf.cache.CacheProcessConfigurer;
import com.hyf.cache.CacheProcessor;
import com.hyf.cache.enums.CacheType;
import com.hyf.cache.impl.contact.CacheContactConstValue;
import com.hyf.cache.impl.contact.redis.CacheRedisMsgReceiver;
import com.hyf.cache.impl.process.CompositeCacheProcessor;
import com.hyf.cache.impl.support.MethodCacheProcessor;
import com.hyf.cache.impl.support.ReuseCacheProcessor;
import com.hyf.cache.impl.support.redis.AdaptiveRedisCacheManager;
import com.hyf.cache.impl.support.redis.RedisCacheKeyExpirationEventMessageListener;
import com.hyf.cache.impl.utils.RedissonLockUtils;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * redis缓存的自动配置
 * 
 * @author baB_hyf
 * @date 2022/02/17
 */
@ConditionalOnClass(RedisConnectionFactory.class)
@ConditionalOnCacheEnabled(CacheType.REDIS)
@Import(RedisCacheAutoConfiguration.ExpireEventListenerConfiguration.class)
public class RedisCacheAutoConfiguration
{

    public static Jackson2JsonRedisSerializer<Object> defaultSerializer() {
        Jackson2JsonRedisSerializer<Object> seria = new Jackson2JsonRedisSerializer<>(Object.class);
        ObjectMapper om = new ObjectMapper();
        om.setVisibility(PropertyAccessor.ALL, Visibility.ANY);
        om.activateDefaultTyping(om.getPolymorphicTypeValidator(), ObjectMapper.DefaultTyping.NON_FINAL);
        seria.setObjectMapper(om);
        return seria;
    }

    @Bean
    @ConditionalOnMissingBean(name = "adaptiveRedisCacheManager")
    public AdaptiveRedisCacheManager adaptiveRedisCacheManager(RedisConnectionFactory redisConnectionFactory) {
        return AdaptiveRedisCacheManager.create(redisConnectionFactory);
    }

    @Bean
    @Order(-100)
    @ConditionalOnMissingBean(name = "redisCacheProcessor")
    public CacheProcessor redisCacheProcessor(CacheProcessConfigurer redisCacheProcessConfigurer) {
        List<CacheProcessor> redisCacheProcessors = Arrays.asList(
                adaptiveRedisMethodCacheProcessor(redisCacheProcessConfigurer), //
                adaptiveRedisReuseCacheProcessor(redisCacheProcessConfigurer) //
        );
        return new CompositeCacheProcessor(redisCacheProcessors);
    }

    public CacheProcessor adaptiveRedisMethodCacheProcessor(CacheProcessConfigurer redisCacheProcessConfigurer) {
        MethodCacheProcessor methodCacheProcessor = new MethodCacheProcessor();
        methodCacheProcessor.setCacheProcessConfigurer(redisCacheProcessConfigurer);
        return methodCacheProcessor;
    }

    public CacheProcessor adaptiveRedisReuseCacheProcessor(CacheProcessConfigurer redisCacheProcessConfigurer) {
        ReuseCacheProcessor reuseCacheProcessor = new ReuseCacheProcessor();
        reuseCacheProcessor.setCacheProcessConfigurer(redisCacheProcessConfigurer);
        return reuseCacheProcessor;
    }

    @Bean
    @Role(BeanDefinition.ROLE_INFRASTRUCTURE)
    public CacheProcessConfigurer redisCacheProcessConfigurer(AdaptiveRedisCacheManager adaptiveRedisCacheManager) {
        return new CacheProcessConfigurer()
        {

            @Override
            public CacheManager getCacheManager() {
                return adaptiveRedisCacheManager;
            }

        };
    }

    // TODO
    @Bean
    @ConditionalOnClass({Redisson.class, RedisProperties.class })
    public RedissonLockUtils redissonLockUtils() {
        return new RedissonLockUtils();
    }

    @Bean
    @ConditionalOnMissingBean(name = "cacheRedisTemplate")
    public RedisTemplate<String, String> cacheRedisTemplate(RedisConnectionFactory factory) {
        RedisTemplate<String, String> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(factory);

        Jackson2JsonRedisSerializer<Object> seria = RedisCacheAutoConfiguration.defaultSerializer();
        // StringRedisSerializer stringRedisSerializer = new
        // StringRedisSerializer(Charset.forName("UTF-8"));

        redisTemplate.setKeySerializer(seria);
        redisTemplate.setValueSerializer(seria);
        redisTemplate.setHashKeySerializer(seria);
        redisTemplate.setHashValueSerializer(seria);
        return redisTemplate;
    }

    /**
     * redis的expire事件监听配置
     */
    public static class ExpireEventListenerConfiguration
    {

        // TODO 名字是否要改变
        @Bean
        @ConditionalOnMissingBean(name = "expireRedisMessageListenerContainer")
        public RedisMessageListenerContainer expireRedisMessageListenerContainer(
                RedisConnectionFactory redisConnectionFactory, MessageListenerAdapter listenerAdapter) {
            RedisMessageListenerContainer redisMessageListenerContainer = new RedisMessageListenerContainer();
            redisMessageListenerContainer.setConnectionFactory(redisConnectionFactory);

            // 订阅多个频道
            // TODO 为了考虑订阅时 频道尚未创建 这边的频道需要是模糊匹配
            // https://docs.spring.io/spring-data/redis/docs/current/reference/html/#pubsub
            redisMessageListenerContainer.addMessageListener(listenerAdapter,
                    new PatternTopic(CacheContactConstValue.REDIS_CONTACT_TOPIC));
            return redisMessageListenerContainer;
        }

        // 表示监听一个频道
        @Bean
        public MessageListenerAdapter listenerAdapter(CacheRedisMsgReceiver messageReceiver) {
            // 这个地方 是给messageListenerAdapter 传入一个消息接受的处理器，利用反射的方法调用“MessageReceive1 ”
            MessageListenerAdapter messageListenerAdapter = new MessageListenerAdapter(messageReceiver, "getMessage");

            return messageListenerAdapter;
        }

        @Bean
        @ConditionalOnMissingBean(name = "redisMessageListenerContainer")
        public RedisCacheKeyExpirationEventMessageListener redisMessageListenerContainer(
                RedisMessageListenerContainer expireRedisMessageListenerContainer) {
            return new RedisCacheKeyExpirationEventMessageListener(expireRedisMessageListenerContainer);
        }

        @Bean
        @ConditionalOnMissingBean(name = "cacheRedisMsgReceiver")
        public CacheRedisMsgReceiver cacheRedisMsgReceiver() {
            return new CacheRedisMsgReceiver();
        }
    }
}

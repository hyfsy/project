package com.hyf.cache.impl.support.redis;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.data.redis.cache.RedisCache;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.cache.RedisCacheWriter;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.util.Assert;

import com.hyf.cache.Cache;
import com.hyf.cache.CacheAdapter;
import com.hyf.cache.CacheManager;
import com.hyf.cache.enums.CacheType;
import com.hyf.cache.impl.context.DynamicCacheConfigContext;
import com.hyf.cache.impl.key.converter.CacheKeyToStringConverter;

/**
 * 扩展RedisCacheManager的Cache对象创建，实现自己的CacheManager接口
 *
 * @author baB_hyf
 * @date 2022/02/10
 * @see AdaptiveRedisCache
 */
public class AdaptiveRedisCacheManager extends RedisCacheManager implements CacheManager
{

    protected final RedisCacheWriter cacheWriter;
    protected final RedisCacheConfiguration defaultCacheConfiguration;

    public AdaptiveRedisCacheManager(RedisCacheWriter cacheWriter, RedisCacheConfiguration defaultCacheConfiguration) {
        super(cacheWriter, defaultCacheConfiguration);
        this.cacheWriter = cacheWriter;
        this.defaultCacheConfiguration = defaultCacheConfiguration;
    }

    public AdaptiveRedisCacheManager(RedisCacheWriter cacheWriter, RedisCacheConfiguration defaultCacheConfiguration, String... initialCacheNames) {
        super(cacheWriter, defaultCacheConfiguration, initialCacheNames);
        this.cacheWriter = cacheWriter;
        this.defaultCacheConfiguration = defaultCacheConfiguration;
    }

    public AdaptiveRedisCacheManager(RedisCacheWriter cacheWriter, RedisCacheConfiguration defaultCacheConfiguration, boolean allowInFlightCacheCreation, String... initialCacheNames) {
        super(cacheWriter, defaultCacheConfiguration, allowInFlightCacheCreation, initialCacheNames);
        this.cacheWriter = cacheWriter;
        this.defaultCacheConfiguration = defaultCacheConfiguration;
    }

    public AdaptiveRedisCacheManager(RedisCacheWriter cacheWriter, RedisCacheConfiguration defaultCacheConfiguration, Map<String, RedisCacheConfiguration> initialCacheConfigurations) {
        super(cacheWriter, defaultCacheConfiguration, initialCacheConfigurations);
        this.cacheWriter = cacheWriter;
        this.defaultCacheConfiguration = defaultCacheConfiguration;
    }

    public AdaptiveRedisCacheManager(RedisCacheWriter cacheWriter, RedisCacheConfiguration defaultCacheConfiguration, Map<String, RedisCacheConfiguration> initialCacheConfigurations, boolean allowInFlightCacheCreation) {
        super(cacheWriter, defaultCacheConfiguration, initialCacheConfigurations, allowInFlightCacheCreation);
        this.cacheWriter = cacheWriter;
        this.defaultCacheConfiguration = defaultCacheConfiguration;
    }

    public static AdaptiveRedisCacheManager create(RedisConnectionFactory connectionFactory) {
        Assert.notNull(connectionFactory, "ConnectionFactory must not be null!");
        RedisCacheWriter redisCacheWriter = RedisCacheWriter.nonLockingRedisCacheWriter(connectionFactory);
        return new AdaptiveRedisCacheManager(redisCacheWriter, defaultCacheConfig());
    }

    /**
     * redis缓存的默认配置
     * 
     * @return 默认配置
     */
    public static RedisCacheConfiguration defaultCacheConfig() {

        // 默认json序列化方式
        RedisCacheConfiguration redisCacheConfiguration = RedisCacheConfiguration.defaultCacheConfig().serializeValuesWith(
                // TODO 动态
                RedisSerializationContext.SerializationPair.fromSerializer(RedisSerializer.json()));
        // 自定义key序列化转换
        redisCacheConfiguration.addCacheKeyConverter(new CacheKeyToStringConverter());

        return redisCacheConfiguration;
    }

    /**
     * 扩展动态创建redis缓存的功能
     *
     * @param name
     *            缓存名称
     * @return redis缓存对象，或null
     */
    @Override
    protected RedisCache getMissingCache(String name) {
        Boolean dynamicCreate = DynamicCacheConfigContext.getDynamicCreate();
        if (dynamicCreate != null && dynamicCreate) {
            return createRedisCache(name, defaultCacheConfiguration);
        }
        return super.getMissingCache(name);
    }

    /**
     * 扩展，返回自定义缓存
     *
     * @param name
     *            缓存名称
     * @param cacheConfig
     *            缓存对象创建所需的配置
     * @return redis缓存对象
     */
    @Override
    protected RedisCache createRedisCache(String name, RedisCacheConfiguration cacheConfig) {
        return new AdaptiveRedisCache(name, cacheWriter, cacheConfig != null ? cacheConfig : defaultCacheConfiguration);
    }

    @Override
    public List<String> getCacheNameList() {
        return new ArrayList<>(getCacheNames());
    }

    @Override
    public Cache findCache(String cacheName) {
        return CacheAdapter.adapt(getCache(cacheName));
    }

    @Override
    public CacheType getCacheType() {
        return CacheType.REDIS;
    }
}

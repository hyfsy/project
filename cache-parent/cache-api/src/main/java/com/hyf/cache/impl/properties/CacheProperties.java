package com.hyf.cache.impl.properties;

import java.time.Duration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

import com.hyf.cache.constants.CacheConstants;
import com.hyf.cache.enums.CacheMode;

/**
 * 缓存属性配置
 * 
 * @author baB_hyf
 * @date 2022/01/18
 */
@ConfigurationProperties(CacheConstants.CACHE_PROPERTY_PREFIX)
public class CacheProperties
{

    @NestedConfigurationProperty
    private final ReuseDegrade reuseDegrade = new ReuseDegrade();

    @NestedConfigurationProperty
    private final Caffeine caffeine = new Caffeine();

    @NestedConfigurationProperty
    private final Redis redis = new Redis();

    private boolean enabled = true;
    // 缓存处理模式
    private CacheMode cacheMode = CacheConstants.DEFAULT_CACHE_MODE;
    private boolean forceConsistency = CacheConstants.FORCE_CONSISTENCY;
    private Class<?> keySerializer = null;
    private Class<?> keyDeserializer = null;
    private Class<?> valueSerializer = null;
    private Class<?> valueDeserializer = null;
    private Duration ttl = null;

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public CacheMode getCacheMode() {
        return cacheMode;
    }

    public void setCacheMode(CacheMode cacheMode) {
        this.cacheMode = cacheMode;
    }

    public boolean isForceConsistency() {
        return forceConsistency;
    }

    public void setForceConsistency(boolean forceConsistency) {
        this.forceConsistency = forceConsistency;
    }

    public Class<?> getKeySerializer() {
        return keySerializer;
    }

    public void setKeySerializer(Class<?> keySerializer) {
        this.keySerializer = keySerializer;
    }

    public Class<?> getKeyDeserializer() {
        return keyDeserializer;
    }

    public void setKeyDeserializer(Class<?> keyDeserializer) {
        this.keyDeserializer = keyDeserializer;
    }

    public Class<?> getValueSerializer() {
        return valueSerializer;
    }

    public void setValueSerializer(Class<?> valueSerializer) {
        this.valueSerializer = valueSerializer;
    }

    public Class<?> getValueDeserializer() {
        return valueDeserializer;
    }

    public void setValueDeserializer(Class<?> valueDeserializer) {
        this.valueDeserializer = valueDeserializer;
    }

    public ReuseDegrade getReuseDegrade() {
        return reuseDegrade;
    }

    public Caffeine getCaffeine() {
        return caffeine;
    }

    public Redis getRedis() {
        return redis;
    }

    public Duration getTtl() {
        return ttl;
    }

    public void setTtl(Duration ttl) {
        this.ttl = ttl;
    }
}

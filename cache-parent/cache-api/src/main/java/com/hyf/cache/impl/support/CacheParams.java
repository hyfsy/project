package com.hyf.cache.impl.support;

import java.util.concurrent.TimeUnit;

/**
 * 包装一些动态的缓存配置参数，方便使用
 *
 * @author baB_hyf
 * @date 2022/02/11
 */
public class CacheParams
{

    private long ttl;
    private TimeUnit unit;
    private boolean random;
    private long randomMillis;
    // private long cacheType;


    public void setTtl(long ttl) {
        this.ttl = ttl;
    }

    public void setUnit(TimeUnit unit) {
        this.unit = unit;
    }

    public void setRandom(boolean random) {
        this.random = random;
    }

    public void setRandomMillis(long randomMillis) {
        this.randomMillis = randomMillis;
    }

    public long getTtl() {
        return ttl;
    }

    public TimeUnit getUnit() {
        return unit;
    }

    public boolean isRandom() {
        return random;
    }

    public long getRandomMillis() {
        return randomMillis;
    }
}

package com.hyf.cache.impl.utils;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;

/**
 * TODO
 *
 * @author baB_hyf
 * @date 2022/03/05
 */
public class RedissonLockUtils
{

    private static RedisProperties redisProperties;

    private static volatile RedissonClient redissonClient = null;

    @Resource
    private RedisProperties temp;

    public static Lock getRedissonLock(Object name) {
        if (!redisEnabled()) {
            throw new UnsupportedOperationException("redis not enabled");
        }

        return getRedissonClient().getLock(name.toString());
    }

    public static ReadWriteLock getRedissonReadWriteLock(Object name) {
        if (!redisEnabled()) {
            throw new UnsupportedOperationException("redis not enabled");
        }

        return getRedissonClient().getReadWriteLock(name.toString());
    }

    // TODO
    private static boolean redisEnabled() {
        return redisProperties != null;
    }

    // TODO
    private static RedissonClient getRedissonClient() {
        if (redissonClient == null) {
            synchronized (RedissonClient.class) {
                if (redissonClient == null) {
                    Config config = new Config();
                    String host = redisProperties.getHost();
                    int port = redisProperties.getPort();
                    int database = redisProperties.getDatabase();
                    config.useSingleServer().setDatabase(database).setAddress("redis://" + host + ":" + port);
                    redissonClient = Redisson.create(config);
                }
            }
        }
        return redissonClient;
    }

    @PostConstruct
    public void post() {
        redisProperties = temp;
    }
}

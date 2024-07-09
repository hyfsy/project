package com.hyf.cache;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;

import com.hyf.cache.enums.CacheType;
import com.hyf.cache.impl.cache.HotKeyCache;
import com.hyf.cache.impl.cache.IndexCache;
import com.hyf.cache.impl.cache.ListenableCache;

/**
 * {@link org.springframework.cache.Cache}的适配器，适配成{@link Cache}
 *
 * TODO 暂时将装饰功能放在这里
 *
 * @author baB_hyf
 * @date 2022/02/10
 * @see org.springframework.cache.Cache
 * @see Cache
 */
public class CacheAdapter implements Cache
{

    private org.springframework.cache.Cache delegate;
    private Cache unwrap;

    public CacheAdapter(org.springframework.cache.Cache cache) {
        delegate = cache;
        this.unwrap = unwrap(cache);
    }

    /**
     * TODO 调用地方需要判断，防止多次包装
     *
     * @param cache
     *            缓存对象
     * @return 适配后的缓存对象
     */
    public static Cache adapt(org.springframework.cache.Cache cache) {
        // TODO 动态
        return new HotKeyCache(new IndexCache(new ListenableCache(new CacheAdapter(cache))));
    }

    /**
     * TODO 调用地方需要判断，防止多次包装
     *
     * @param cache
     *            缓存对象
     * @return 适配后的缓存对象
     */
    public static Cache adaptNotListener(org.springframework.cache.Cache cache) {
        // TODO 动态
        return new HotKeyCache(new IndexCache(new CacheAdapter(cache)));
    }

    /**
     * TODO 调用地方需要判断，防止多次包装
     *
     * @param caches
     *            缓存对象列表
     * @return 适配后的缓存对象列表
     */
    public static List<Cache> adapt(Collection<? extends org.springframework.cache.Cache> caches) {
        List<Cache> adaptedCacheList = new ArrayList<>(caches.size());

        for (org.springframework.cache.Cache cache : caches) {
            if (cache instanceof CacheAdapter) {
                adaptedCacheList.add((CacheAdapter) cache);
            }
            else {
                adaptedCacheList.add(adapt(cache));
            }
        }

        return adaptedCacheList;
    }

    private Cache unwrap(org.springframework.cache.Cache cache) {
        if (cache instanceof Cache) {
            return (Cache) cache;
        }

        Cache unwrap = doUnwrap(cache, cache.getClass().getFields());
        if (unwrap == null) {
            unwrap = doUnwrap(cache, cache.getClass().getDeclaredFields());
        }

        return unwrap;
    }

    @Override
    public void put(Object key, Object value, long ttl, TimeUnit unit) {
        if (unwrap != null) {
            unwrap.put(key, value, ttl, unit);
        }
    }

    @Override
    public void put(Object key, Object value, long ttl, TimeUnit unit, long randomMillis) {
        if (unwrap != null) {
            unwrap.put(key, value, ttl, unit, randomMillis);
        }
    }

    @Override
    public ValueWrapper putIfAbsent(Object key, Object value, long ttl, TimeUnit unit) {
        if (unwrap != null) {
            return unwrap.putIfAbsent(key, value, ttl, unit);
        }
        return () -> null;
    }

    @Override
    public ValueWrapper putIfAbsent(Object key, Object value, long ttl, TimeUnit unit, long randomMillis) {
        if (unwrap != null) {
            return unwrap.putIfAbsent(key, value, ttl, unit, randomMillis);
        }
        return () -> null;
    }

    @Override
    public void touch(Object key, long ttl, TimeUnit unit) {
        if (unwrap != null) {
            unwrap.touch(key, ttl, unit);
        }
    }

    @Override
    public ValueWrapper getAll() {
        if (unwrap != null) {
            return unwrap.getAll();
        }
        return () -> null;
    }

    @Override
    public CacheType getCacheType() {
        if (unwrap != null) {
            return unwrap.getCacheType();
        }
        return CacheType.NONE;
    }

    @Override
    public Lock getLock(Object lockName) {
        if (unwrap != null) {
            return unwrap.getLock(lockName);
        }
        return null;
    }

    @Override
    public ReadWriteLock getReadWriteLock(Object lockName) {
        if (unwrap != null) {
            return unwrap.getReadWriteLock(lockName);
        }
        return null;
    }

    @Override
    public String getName() {
        return delegate.getName();
    }

    @Override
    public Object getNativeCache() {
        return delegate.getNativeCache();
    }

    @Override
    public ValueWrapper get(Object key) {
        return delegate.get(key);
    }

    @Override
    public <T> T get(Object key, Class<T> type) {
        return delegate.get(key, type);
    }

    @Override
    public <T> T get(Object key, Callable<T> valueLoader) {
        return delegate.get(key, valueLoader);
    }

    @Override
    public void put(Object key, Object value) {
        delegate.put(key, value);
    }

    @Override
    public ValueWrapper putIfAbsent(Object key, Object value) {
        return delegate.putIfAbsent(key, value);
    }

    @Override
    public void evict(Object key) {
        delegate.evict(key);
    }

    @Override
    public void clear() {
        delegate.clear();
    }

    private Cache doUnwrap(org.springframework.cache.Cache cache, Field[] fields) {
        for (Field field : fields) {
            if (field.getDeclaringClass().isAssignableFrom(org.springframework.cache.Cache.class)) {
                try {
                    field.setAccessible(true);
                    Cache c = unwrap((org.springframework.cache.Cache) field.get(cache));
                    if (c != null) {
                        return c;
                    }
                }
                catch (IllegalAccessException ignored) {
                }
            }
        }

        return null;
    }
}

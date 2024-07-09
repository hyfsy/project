package com.hyf.cache.impl.cache;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.AnnotationAwareOrderComparator;

import com.hyf.cache.CacheEvent;
import com.hyf.cache.CacheListener;
import com.hyf.cache.CacheValueWrapper;
import com.hyf.cache.enums.CacheEventType;
import com.hyf.cache.enums.CacheType;

/**
 * 缓存事件发布器
 *
 * @author baB_hyf
 * @date 2022/02/15
 * @see CacheListener
 * @see CacheEvent
 * @see ListenableCache
 */
public class CacheEventPublisher
{

    /** 全局缓存监听器列表 */
    private static final List<CacheListener> cacheListenerList = new CopyOnWriteArrayList<>();

    /** spring注册的缓存监听器列表 */
    @Autowired(required = false)
    private List<CacheListener> ownListenerList = new ArrayList<>();

    /**
     * 缓存事件的发布
     *
     * @param cacheEventType
     *            缓存事件类型
     * @param cacheName
     *            缓存名称
     * @param cacheType
     *            缓存类型
     * @param key
     *            缓存key
     * @param oldValue
     *            缓存旧值
     * @param newValue
     *            当前的缓存新值
     */
    public static void publishEvent(CacheEventType cacheEventType, String cacheName, CacheType cacheType, Object key,
            Object oldValue, Object newValue) {
        cacheListenerList.stream().filter(l -> l.interest(cacheType, cacheName, key)).forEach(l -> {
            CacheValueWrapper oldCacheValueWrapper = null;
            if (oldValue != null) {
                oldCacheValueWrapper = new CacheValueWrapper(cacheType, oldValue);
            }
            CacheValueWrapper newCacheValueWrapper = null;
            if (newValue != null) {
                newCacheValueWrapper = new CacheValueWrapper(cacheType, newValue);
            }

            CacheEvent cacheEvent = new CacheEvent(cacheName, key, cacheType, cacheEventType, oldCacheValueWrapper,
                    newCacheValueWrapper);

            if (CacheEventType.HIT.equals(cacheEventType)) {
                l.onHit(cacheEvent);
            }
            else if (CacheEventType.CREATE.equals(cacheEventType)) {
                l.onCreated(cacheEvent);
            }
            else if (CacheEventType.MODIFY.equals(cacheEventType)) {
                l.onModified(cacheEvent);
            }
            else if (CacheEventType.EVICT.equals(cacheEventType)) {
                l.onEvicted(cacheEvent);
            }
            else if (CacheEventType.EXPIRE.equals(cacheEventType)) {
                l.onExpired(cacheEvent);
            }
        });
    }

    /**
     * 添加全局缓存监听器
     * 
     * @param listener
     *            全局缓存监听器
     */
    public static void addGlobalListener(CacheListener listener) {
        cacheListenerList.add(listener);
        sort(cacheListenerList);
    }

    /**
     * 删除全局缓存监听器
     * 
     * @param listener
     *            全局缓存监听器
     */
    public static void removeGlobalListener(CacheListener listener) {
        cacheListenerList.remove(listener);
    }

    /**
     * 获取全局缓存监听器列表
     * 
     * @return 全局缓存监听器
     */
    public static List<CacheListener> getGlobalCacheListeners() {
        return cacheListenerList;
    }

    private static void sort(List<CacheListener> listeners) {
        AnnotationAwareOrderComparator.sort(listeners);
    }

    @PostConstruct
    public void post() {
        cacheListenerList.addAll(ownListenerList);
        sort(cacheListenerList);
    }
}

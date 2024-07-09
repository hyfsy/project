package com.hyf.cache.impl.context;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 动态缓存配置上下文，缓存操作时使用，基于{@link ThreadLocal}
 *
 * @author baB_hyf
 * @date 2022/02/10
 * @see com.hyf.cache.CacheManager
 * @see com.hyf.cache.Cache
 */
public class DynamicCacheConfigContext
{

    public static final String TTL = "dynamic_config_ttl";
    public static final String TTL_UNIT = "dynamic_config_ttl_unit";
    public static final String ALLOW_NULL = "dynamic_config_allow_null";
    public static final String DYNAMIC_CREATE = "dynamic_config_dynamic_create";

    private static final ThreadLocal<Map<String, Object>> dynamicConfigThreadLocal = ThreadLocal
            .withInitial(HashMap::new);

    public static Object put(String key, Object param) {
        return dynamicConfigThreadLocal.get().put(key, param);
    }

    public static Object get(String key) {
        return dynamicConfigThreadLocal.get().get(key);
    }

    public static <T> T get(String key, Class<T> clazz) {
        return clazz.cast(dynamicConfigThreadLocal.get().get(key));
    }

    public static void remove() {
        dynamicConfigThreadLocal.remove();
    }

    public static void remove(String key) {
        dynamicConfigThreadLocal.get().remove(key);
    }

    public static Long getTTL() {
        return get(DynamicCacheConfigContext.TTL, Long.class);
    }

    public static TimeUnit getTTLUnit() {
        return get(DynamicCacheConfigContext.TTL_UNIT, TimeUnit.class);
    }

    public static Boolean getAllowNull() {
        return get(DynamicCacheConfigContext.ALLOW_NULL, Boolean.class);
    }

    public static void setAllowNull(boolean allowNull) {
        put(DynamicCacheConfigContext.ALLOW_NULL, allowNull);
    }

    public static Boolean getDynamicCreate() {
        return get(DynamicCacheConfigContext.DYNAMIC_CREATE, Boolean.class);
    }

    public static void setDynamicCreate(boolean dynamicCreate) {
        put(DynamicCacheConfigContext.DYNAMIC_CREATE, dynamicCreate);
    }

    public static void removeDynamicCreate() {
        remove(DynamicCacheConfigContext.DYNAMIC_CREATE);
    }
}

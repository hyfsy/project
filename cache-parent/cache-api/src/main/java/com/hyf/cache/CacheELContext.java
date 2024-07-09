package com.hyf.cache;

import java.util.HashMap;
import java.util.Map;

/**
 * only user use
 *
 * 提供spel表达式使用的上下文供用户自定义使用
 * 
 * @author baB_hyf
 * @date 2022/01/16
 */
public class CacheELContext
{

    private static final ThreadLocal<Map<String, Object>> extraParamsThreadLocal = ThreadLocal
            .withInitial(HashMap::new);

    public static Object put(String key, Object param) {
        return extraParamsThreadLocal.get().put(key, param);
    }

    public static Object get(String key) {
        return extraParamsThreadLocal.get().get(key);
    }

    public static Map<String, Object> get() {
        return extraParamsThreadLocal.get();
    }

    public static Object remove(String key) {
        return extraParamsThreadLocal.get().remove(key);
    }

    public static Map<String, Object> clear() {
        Map<String, Object> extraParamsMap = get();
        extraParamsMap.clear();
        return extraParamsMap;
    }

    public static Map<String, Object> remove() {
        Map<String, Object> extraParamsMap = get();
        extraParamsThreadLocal.remove();
        return extraParamsMap;
    }
}

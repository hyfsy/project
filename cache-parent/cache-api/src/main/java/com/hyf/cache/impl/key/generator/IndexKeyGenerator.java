package com.hyf.cache.impl.key.generator;

import java.lang.reflect.Method;

import com.hyf.cache.KeyGenerator;
import com.hyf.cache.impl.key.IndexCacheKey;

/**
 * 全局索引key生成器
 * 
 * @author baB_hyf
 * @date 2022/02/11
 */
public class IndexKeyGenerator implements KeyGenerator
{

    private String type;
    private String cacheName;

    public IndexKeyGenerator(String type, String cacheName) {
        this.type = type;
        this.cacheName = cacheName;
    }

    @Override
    public Object generate(Object target, Method method, Object... params) {
        return new IndexCacheKey(cacheName, type);
    }
}

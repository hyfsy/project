package com.hyf.cache.bus;

/**
 * 可复用缓存索引主键获取
 * 
 * @author baB_hyf
 * @date 2022/02/09
 */
public interface CacheClassIndexMapper<T>
{

    String getIndexName(Class<T> clazz);
}

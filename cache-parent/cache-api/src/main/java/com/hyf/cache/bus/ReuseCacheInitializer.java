package com.hyf.cache.bus;

import java.util.Map;
import java.util.Set;

/**
 * 可复用缓存的全量初始化同步器
 *
 * @author baB_hyf
 * @date 2022/01/20
 */
public interface ReuseCacheInitializer<T>
{

    /**
     * 可复用缓存
     * 
     * @param dataMap
     *            数据map
     * @param indexMap
     *            索引map
     * @param multiIndexMap
     *            组合索引map
     */
    void init(Map<String, T> dataMap, Map<String, String> indexMap, Map<String, Set<String>> multiIndexMap);
}

package com.hyf.cache.impl.index.bus;

import java.util.Set;

/**
 * TODO 可复用索引缓存
 * 
 * @author baB_hyf
 * @date 2022/02/11
 */
public interface ReuseIndexCache
{

    /**
     * 获取业务对象的索引列表
     * 
     * @param fieldName
     *            字段名称
     * @param fieldValue
     *            字段值
     * @return 业务对象的索引列表
     */
    Set<String> getIndex(String fieldName, String fieldValue);

    /**
     * 获取业务对象的组合索引列表
     *
     * @param fieldName
     *            字段名称
     * @param fieldValue
     *            字段值
     * @return 业务对象的组合索引列表
     */
    Set<String> getCompositeIndex(String[] fieldName, String[] fieldValue);

}

package com.hyf.cache.impl.process;

import com.hyf.cache.CacheProcessor;
import com.hyf.cache.enums.CacheType;

/**
 * 缓存处理器顺序构建
 * 
 * @author baB_hyf
 * @date 2022/02/16
 * @see com.hyf.cache.CacheProcessChain
 * @see CacheProcessor
 */
public interface ChainOrderBuilder
{

    /**
     * 给定缓存类型的列表，自动构建对应顺序的缓存处理链
     * 
     * @return 顺序的缓存类型的列表
     */
    CacheType[] buildWithCacheType();

    /**
     * 获取自定义缓存处理器对象，在CacheType.CUSTOM存在时使用
     *
     * @return 自定义缓存处理器类
     */
    default CacheProcessor getCustomProcessor() {
        return null;
    }
}

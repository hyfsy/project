package com.hyf.cache.custom;

import com.hyf.cache.enums.CacheType;
import com.hyf.cache.impl.process.ChainOrderBuilder;

/**
 * @author baB_hyf
 * @date 2022/02/17
 */
// @Component
public class CustomChainOrderBuilder implements ChainOrderBuilder
{

    @Override
    public CacheType[] buildWithCacheType() {
        return new CacheType[] { //
                CacheType.CAFFEINE, CacheType.REDIS //
                // , //
        };
    }
}

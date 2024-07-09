package com.hyf.cache.custom;

import com.hyf.cache.bus.CacheClassIndexMapper;
import com.hyf.cache.test.entity.BaseEntity;

/**
 * @author baB_hyf
 * @date 2022/02/09
 */
public class EntityClassIndexMapper implements CacheClassIndexMapper<BaseEntity>
{

    @Override
    public String getIndexName(Class<BaseEntity> clazz) {
        return "rowGuid";
    }
}

package com.hyf.cache.impl.key.converter;

import org.springframework.core.convert.converter.Converter;

import com.hyf.cache.impl.key.AbstractCacheKey;

/**
 * 缓存key到{@link String}类型的转换器，redis序列化使用
 * 
 * @author baB_hyf
 * @date 2022/02/15
 * @see org.springframework.data.redis.cache.RedisCacheConfiguration
 */
public class CacheKeyToStringConverter implements Converter<AbstractCacheKey, String>
{

    @Override
    public String convert(/* TODO */ AbstractCacheKey source) {
        return source.toString();
    }
}

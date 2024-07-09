package com.hyf.cache.impl.key.converter;

import java.nio.charset.StandardCharsets;

import org.springframework.core.convert.converter.Converter;

import com.hyf.cache.CacheKey;
import com.hyf.cache.impl.CacheKeyParser;

/**
 * @author baB_hyf
 * @date 2022/02/15
 */
@Deprecated
public class ByteArrayToAbstractKeyConverter implements Converter<byte[], CacheKey>
{
    @Override
    public CacheKey convert(byte[] source) {
        String s = new String(source, StandardCharsets.UTF_8);
        return CacheKeyParser.parse(s);
    }
}

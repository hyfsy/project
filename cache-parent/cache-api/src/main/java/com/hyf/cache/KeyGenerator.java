package com.hyf.cache;

/**
 * 自定义key生成策略
 *
 * @author baB_hyf
 * @date 2022/01/14
 */
public interface KeyGenerator extends org.springframework.cache.interceptor.KeyGenerator
{
    // TODO 提供啥工具方法，自动构建相关的key
}

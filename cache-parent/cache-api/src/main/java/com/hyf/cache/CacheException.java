package com.hyf.cache;

/**
 * 缓存相关异常
 * 
 * @author baB_hyf
 * @date 2022/01/14
 */
public class CacheException extends RuntimeException
{

    public CacheException(String message) {
        super(message);
    }

    public CacheException(String message, Throwable cause) {
        super(message, cause);
    }

    public CacheException(Throwable cause) {
        super(cause);
    }
}

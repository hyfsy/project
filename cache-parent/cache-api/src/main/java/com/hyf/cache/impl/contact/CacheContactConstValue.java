package com.hyf.cache.impl.contact;

import java.util.UUID;

/**
 * 描述了缓存消息传递涉及到的常量
 * 
 * @author 99515
 * @version 2022年3月3日
 */
public class CacheContactConstValue
{
    /**
     * redis订阅的频道，此处加上模糊匹配
     */
    public static final String REDIS_CONTACT_TOPIC = "Cache1.0_CONTACT_TOPIC*";

    /**
     * REDIS连接端的标识
     */
    public static final String REDIS_CLIENT_ID = UUID.randomUUID().toString();
}

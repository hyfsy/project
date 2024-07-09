package com.hyf.cache.impl.contact;

public class CacheContactMsg
{

    private String clientId = null;

    private MsgType msgType;

    private String cacheKey;

    private String cacheName;

    public CacheContactMsg() {
        super();
    }

    public CacheContactMsg(MsgType msgType, String cacheKey, String cacheName) {
        super();
        this.msgType = msgType;
        this.cacheKey = cacheKey;
        this.cacheName = cacheName;
        this.clientId = CacheContactConstValue.REDIS_CLIENT_ID;
    }

    public enum MsgType
    {
        DELETE;
    }

    public MsgType getMsgType() {
        return msgType;
    }

    public String getCacheKey() {
        return cacheKey;
    }

    public String getCacheName() {
        return cacheName;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public void setMsgType(MsgType msgType) {
        this.msgType = msgType;
    }

    public void setCacheKey(String cacheKey) {
        this.cacheKey = cacheKey;
    }

    public void setCacheName(String cacheName) {
        this.cacheName = cacheName;
    }

}

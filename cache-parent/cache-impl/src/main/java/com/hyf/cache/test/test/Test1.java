package com.hyf.cache.test.test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.hyf.cache.test.entity.FrameOu;
import com.hyf.cache.test.entity.FrameUser;
import com.hyf.cache.bus.annotation.ReuseCacheable;

/**
 * @author baB_hyf
 * @date 2022/01/18
 */
public class Test1
{

    /** userguid -> entity */
    Map<String, Object> caches = new HashMap<>();

    /** ouguid -> userguid */
    Map<String, String> idx_OuGuid = new HashMap<>();

    public static void main(String[] args) {
    }

    @ReuseCacheable(cacheNames = "user",
            mapperIdx = "#loginId",
            cacheClass = FrameUser.class)
    public FrameUser getUserByLoginId(String loginId) {
        return null;
    }

    @ReuseCacheable(cacheNames = "ou",
            mapperIdx = "#ouGuid")
    public FrameOu getOuByOuGuid(String ouGuid) {
        return null;
    }

    @ReuseCacheable(cacheNames = "ou",
            mapperIdx = "#ouGuid",
            cacheClass = FrameOu.class, cacheProperty = "#ouName")
    public String getOuNameByOuGuid(String ouGuid) {
        return null;
    }

    @ReuseCacheable(cacheNames = "ou",
            mapperIdx = "",
            cacheClass = FrameOu.class)
    public List<FrameOu> listAllOu() {
        return null;
    }

    @ReuseCacheable(cacheNames = "ou",
            mapperIdx = "#typeGuid",
            cacheClass = FrameOu.class)
    public List<FrameOu> listAllOuByTypeGuid(String typeGuid) {
        return null;
    }

    @ReuseCacheable(cacheNames = "user",
            mapperIdx = "#ouGuid",
            cacheClass = FrameUser.class)
    public List<FrameUser> getUserByOuGuid(String ouGuid) {
        return null;
    }

    @ReuseCacheable(cacheNames = "user",
            mapperIdx = "#ouCode",
            cacheClass = FrameUser.class)
    public List<FrameUser> getUserByOuCode(String ouCode) {
        return null;
    }

    @ReuseCacheable(cacheNames = "user",
            mapperIdx = {"#ouGuid", "#loginId" },
            cacheClass = FrameUser.class)
    public FrameUser getUserByOuGuidAndLoginId(String ouGuid, String loginId) {
        return null;
    }
}

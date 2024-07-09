package com.hyf.cache.impl.utils;

/**
 * 字符串工具类
 * 
 * @author baB_hyf
 * @date 2022/02/08
 */
public class StringUtils
{

    public static boolean isBlank(String str) {
        if (str == null || "".equals(str) || "".equals(str.trim())) {
            return true;
        }
        return false;
    }

    public static boolean isNotBlank(String str) {
        return !isBlank(str);
    }
}

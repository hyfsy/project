package com.hyf.frame;

import java.lang.reflect.Method;
import java.lang.reflect.Type;

/**
 * @author baB_hyf
 * @date 2022/02/11
 */
public class T4 {

    public static void main(String[] args) {
        Method[] declaredMethods = T4.class.getDeclaredMethods();
        Type genericReturnType = declaredMethods[2].getGenericReturnType();
        Class clazz = (Class)genericReturnType;
        boolean array = clazz.isArray();
        System.out.println();
    }

    public String[] sss() {
        return new String[]{};
    }

    public int aaa() {
        return 0;
    }
}

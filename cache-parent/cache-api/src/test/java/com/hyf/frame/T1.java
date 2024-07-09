package com.hyf.frame;

import java.lang.reflect.Method;
import java.util.List;

/**
 * @author baB_hyf
 * @date 2022/02/09
 */
public class T1 {

    public static void main(String[] args) {
        Method[] methods = T1.class.getMethods();
methods[1].getGenericReturnType();
        System.out.println();
    }

    public List<T1> aaa() {
        return null;
    }

    public T1 bbb() {return null;}

    public void ccc() {}
}

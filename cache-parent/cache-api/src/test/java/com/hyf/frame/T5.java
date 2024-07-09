package com.hyf.frame;

import com.hyf.cache.impl.utils.StringUtils;

import java.util.Arrays;

/**
 * @author baB_hyf
 * @date 2022/02/15
 */
public class T5 {

    public static void main(String[] args) {

        String cma = "com.hyf.Abc.methodName()";
        // String cma = "com.hyf.Abc.methodName(1)";
        // String cma = "com.hyf.Abc.methodName(1,1)";

        int i = cma.lastIndexOf("(");

        Object[] as;
        String a = cma.substring(i + 1, cma.length() - 1);
        if (StringUtils.isBlank(a)) {
            as = new Object[0];
        }
        else {
            String[] argsStr = a.split(",");
            as = new Object[argsStr.length];
            System.arraycopy(argsStr, 0, as, 0, argsStr.length);
        }

        String cm = cma.substring(0, i);
        String c = cm.substring(0, cm.lastIndexOf('.'));
        String m = cm.substring(cm.lastIndexOf('.') + 1);
        System.out.println(c);
        System.out.println(m);
        System.out.println(Arrays.toString(as));
    }

}

package com.hyf.cache.impl.key;

import java.lang.reflect.Method;
import java.util.Arrays;

import org.springframework.util.StringUtils;

/**
 * 处理 class / method / args 相关的缓存key
 * 
 * @author baB_hyf
 * @date 2022/02/15
 */
public class CMA
{

    private final String className;
    private final String methodName;
    private final Object[] args;

    public CMA(Class<?> className, Method methodName, Object[] args) {
        this.className = className.getName();
        this.methodName = methodName.getName();
        this.args = args;
    }

    public CMA(String className, String methodName, Object[] args) {
        this.className = className;
        this.methodName = methodName;
        this.args = args;
    }

    /**
     * 解析字符串
     * 
     * @param cmaSegment
     *            fullClassName.methodName(arg1, arg2)
     * @return CMA对象
     */
    public static CMA parse(String cmaSegment) {
        try {

            Object[] a = null;
            String c = null;
            String m = null;

            int argsCursor = cmaSegment.lastIndexOf('(');

            // no args
            if (argsCursor == -1) {
                argsCursor = cmaSegment.length();
            }
            else {
                String as = cmaSegment.substring(argsCursor + 1, cmaSegment.length() - 1);
                if (StringUtils.hasText(as)) {
                    String[] argsStr = Arrays.stream(as.split(",")).map(String::trim).toArray(String[]::new);
                    a = new Object[argsStr.length];
                    System.arraycopy(argsStr, 0, a, 0, argsStr.length);
                }
                else {
                    a = new Object[0];
                }
            }

            int methodCursor = cmaSegment.lastIndexOf('.', argsCursor);

            // no class
            if (methodCursor == -1) {
                m = cmaSegment;
            }
            else {
                c = cmaSegment.substring(0, methodCursor);
                m = cmaSegment.substring(methodCursor + 1, argsCursor);
            }

            return new CMA(c, m, a);
        }
        catch (Exception e) {
            throw new IllegalArgumentException(cmaSegment);
        }
    }

    public static String toString(Class<?> className, Method methodName, Object[] params) {
        return toString(className.getName(), methodName.getName(), params);
    }

    public static String toString(String className, String methodName, Object[] params) {
        StringBuilder sb = new StringBuilder();
        sb.append(className);
        sb.append('.');
        sb.append(methodName);
        sb.append('(');
        sb.append(StringUtils.arrayToCommaDelimitedString(params));
        sb.append(')');
        return sb.toString();
    }

    public static String toClassMethodString(String className, String methodName) {
        return className + "." + methodName;
    }

    public static String toMethodArgsString(String methodName, Object[] args) {
        return methodName + toArgsString(args);
    }

    public static String toArgsString(Object[] args) {
        return "(" + StringUtils.arrayToCommaDelimitedString(args) + ")";
    }

    public String getClassName() {
        return className;
    }

    public String getMethodName() {
        return methodName;
    }

    public Object[] getArgs() {
        return args;
    }

    @Override
    public String toString() {
        return CMA.toString(getClassName(), getMethodName(), getArgs());
    }
}

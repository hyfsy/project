package com.hyf.cache.impl.utils;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.Map;

/**
 * TODO GenericTypeResolver 重构
 *
 * 泛型工具类
 * 
 * @author baB_hyf
 * @date 2022/02/11
 */
public class TypeUtils
{

    public static Class<?> getMethodActualReturnType(Method method) {

        Class<?> clazz = null;

        Type genericReturnType = method.getGenericReturnType();
        if (genericReturnType instanceof ParameterizedType) {
            ParameterizedType parameterizedType = (ParameterizedType) genericReturnType;

            // 暂不支持嵌套泛型
            if (parameterizedType.getRawType() instanceof Class) {
                Class<?> parameterizedClass = (Class<?>) parameterizedType.getRawType();

                Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();
                if (actualTypeArguments.length > 0) {

                    if (parameterizedClass.isAssignableFrom(Collection.class)) {
                        if (actualTypeArguments.length == 1) {
                            clazz = (Class<?>) actualTypeArguments[0];
                        }
                    }
                    else if (parameterizedClass.isAssignableFrom(Map.class)) {
                        if (actualTypeArguments.length == 2) {
                            clazz = (Class<?>) actualTypeArguments[1];
                        }
                    }
                }
            }
        }
        // else if (genericReturnType instanceof Class) {
        // Class<?> returnType = (Class<?>) genericReturnType;
        // if (returnType.isArray()) {
        // returnType = returnType.getComponentType();
        // }
        //
        // // check simple class
        //
        // }

        return clazz;
    }
}

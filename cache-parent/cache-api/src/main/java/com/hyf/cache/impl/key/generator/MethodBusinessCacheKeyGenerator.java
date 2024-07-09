package com.hyf.cache.impl.key.generator;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Set;
import java.util.regex.Pattern;

import org.springframework.context.expression.AnnotatedElementKey;
import org.springframework.expression.EvaluationContext;

import com.hyf.cache.BaseCacheOperation;
import com.hyf.cache.InvocationContext;
import com.hyf.cache.OperationBasedKeyGenerator;
import com.hyf.cache.impl.context.DefaultInvocationContext;
import com.hyf.cache.impl.key.CompositeCacheKey;
import com.hyf.cache.impl.key.MethodBusinessCacheKey;
import com.hyf.cache.impl.spel.EvaluationManager;
import com.hyf.cache.impl.utils.StringUtils;

/**
 * 方法业务key生成器
 *
 * @author baB_hyf
 * @date 2022/02/15
 */
public class MethodBusinessCacheKeyGenerator extends OperationBasedKeyGenerator<BaseCacheOperation>
{

    private static final Pattern classNamePattern = Pattern.compile("\\[C\\]");
    private static final Pattern methodNamePattern = Pattern.compile("\\[M\\]");
    private static final Pattern classAndMethodPattern = Pattern.compile("\\[C:M\\]");

    public MethodBusinessCacheKeyGenerator(BaseCacheOperation operation) {
        super(operation);
    }

    @Override
    public Object generate(Object o, Method method, Object... params) {

        InvocationContext invocationContext = DefaultInvocationContext.simple(o, method, params);
        String key = operation.getKey();
        Object id = "";
        Object[] args = invocationContext.getArgs();

        if (!StringUtils.isBlank(key)) {
            // TODO 此处的spel表达式先处理成常量 然后再去调用下面的方法生成新Key
            // ClassName
            // #root.targetClass.getName()+'.'+#root.methodName
            boolean custom = false;
            if (classNamePattern.matcher(key).find()) {
                key = key.replaceAll("\\[C\\]", "#root.targetClass.getName()");
                custom = true;
            }
            // MethodName
            if (methodNamePattern.matcher(key).find()) {
                key = key.replaceAll("\\[M\\]", "#root.methodName");
                custom = true;
            }
            if (classAndMethodPattern.matcher(key).find()) {
                key = key.replaceAll("\\[C\\:M\\]", "#root.targetClass.getName()+':'+#root.methodName");
                custom = true;
            }
            if (!custom) {
                key = "#root.targetClass.getName()+':'+#root.methodName+':'+" + key;
            }

            EvaluationContext evaluationContext = EvaluationManager.createEvaluationContext(invocationContext);
            id = EvaluationManager.key(key, new AnnotatedElementKey(invocationContext.getMethod(), invocationContext.getTargetClass()), evaluationContext);
            args = new Object[] {id };
        }
        else {
            // + ":" + CMA.toArgsString(getArgs())
            id = method.getDeclaringClass().getName() + ":" + method.getName() + ":" + Arrays.toString(params);
        }

        CompositeCacheKey compositeCacheKey = new CompositeCacheKey();

        Set<String> cacheNames = operation.getCacheNames();
        for (String cacheName : cacheNames) {
            compositeCacheKey.add(new MethodBusinessCacheKey(cacheName, operation.getCacheClassName(), id.toString(), args));
        }

        return compositeCacheKey;
    }
}

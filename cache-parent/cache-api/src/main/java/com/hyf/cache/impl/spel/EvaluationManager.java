package com.hyf.cache.impl.spel;

import java.lang.reflect.Method;
import java.util.Collection;

import com.hyf.cache.Cache;
import com.hyf.cache.impl.context.DefaultInvocationContext;
import com.hyf.cache.InvocationContext;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.expression.AnnotatedElementKey;
import org.springframework.expression.EvaluationContext;

/**
 * @author baB_hyf
 * @date 2022/02/09
 */
public class EvaluationManager
{

    private static final CacheOperationExpressionEvaluator evaluator = new CacheOperationExpressionEvaluator();

    public static EvaluationContext createEvaluationContext(Object target, Method method, Object[] args) {
        InvocationContext invocationContext = new DefaultInvocationContext(target, method, args);
        return createEvaluationContext(null, invocationContext,null);
    }

    public static EvaluationContext createEvaluationContext(InvocationContext invocationContext) {
        return createEvaluationContext(null, invocationContext ,null);
    }

    public static EvaluationContext createEvaluationContext(Collection<? extends Cache> caches, InvocationContext invocationContext,
                                                            BeanFactory beanFactory) {
        return evaluator.createEvaluationContext(caches, invocationContext, beanFactory);
    }

    public static Object key(String keyExpression, AnnotatedElementKey methodKey, EvaluationContext evalContext) {
        return evaluator.key(keyExpression, methodKey, evalContext);
    }

    public static boolean condition(String conditionExpression, AnnotatedElementKey methodKey, EvaluationContext evalContext) {
        return evaluator.condition(conditionExpression, methodKey, evalContext);
    }

    public static boolean unless(String unlessExpression, AnnotatedElementKey methodKey, EvaluationContext evalContext) {
        return evaluator.unless(unlessExpression, methodKey, evalContext);
    }

    public static Object eval(String expression, AnnotatedElementKey methodKey, EvaluationContext evalContext) {
        return evaluator.eval(expression, methodKey, evalContext);
    }
}

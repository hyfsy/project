package com.hyf.cache.impl.spel;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.hyf.cache.CacheELContext;
import com.hyf.cache.InvocationContext;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.cache.Cache;
import org.springframework.context.expression.AnnotatedElementKey;
import org.springframework.context.expression.BeanFactoryResolver;
import org.springframework.context.expression.CachedExpressionEvaluator;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;

/**
 * @author baB_hyf
 * @date 2022/02/08
 */
public class CacheOperationExpressionEvaluator extends CachedExpressionEvaluator
{

    /**
     * Indicate that there is no result variable.
     */
    public static final Object NO_RESULT = new Object();

    /**
     * Indicate that the result variable cannot be used at all.
     */
    public static final Object RESULT_UNAVAILABLE = new Object();

    /**
     * The name of the variable holding the result object.
     */
    public static final String RESULT_VARIABLE = "result";

    private final Map<ExpressionKey, Expression> keyCache = new ConcurrentHashMap<>(64);

    private final Map<ExpressionKey, Expression> conditionCache = new ConcurrentHashMap<>(64);

    private final Map<ExpressionKey, Expression> unlessCache = new ConcurrentHashMap<>(64);

    private final Map<ExpressionKey, Expression> evalCache = new ConcurrentHashMap<>(64);

    public EvaluationContext createEvaluationContext(Collection<? extends Cache> caches,
                                                     InvocationContext invocationContext, BeanFactory beanFactory) {
        return createEvaluationContext(caches, invocationContext.getMethod(), invocationContext.getArgs(),
                invocationContext.getTarget(), invocationContext.getTargetClass(), invocationContext.getTargetMethod(),
                invocationContext.getResult(), beanFactory);
    }

    /**
     * Create an {@link EvaluationContext}.
     * 
     * @param caches
     *            the current caches
     * @param method
     *            the method
     * @param args
     *            the method arguments
     * @param target
     *            the target object
     * @param targetClass
     *            the target class
     * @param result
     *            the return value (can be {@code null}) or {@link #NO_RESULT} if
     *            there is no return at this time
     * @return the evaluation context
     */
    public EvaluationContext createEvaluationContext(Collection<? extends Cache> caches, Method method, Object[] args,
            Object target, Class<?> targetClass, Method targetMethod, Object result,
            BeanFactory beanFactory) {

        CacheExpressionRootObject rootObject = new CacheExpressionRootObject(caches, method, args, target, targetClass);
        CacheEvaluationContext evaluationContext = new CacheEvaluationContext(rootObject, targetMethod, args,
                getParameterNameDiscoverer());
        if (result == RESULT_UNAVAILABLE) {
            evaluationContext.addUnavailableVariable(RESULT_VARIABLE);
        }
        else if (result != NO_RESULT) {
            evaluationContext.setVariable(RESULT_VARIABLE, result);
        }

        // 额外的用户自定义
        evaluationContext.setVariables(CacheELContext.get());

        if (beanFactory != null) {
            evaluationContext.setBeanResolver(new BeanFactoryResolver(beanFactory));
        }
        return evaluationContext;
    }

    public Object key(String keyExpression, AnnotatedElementKey methodKey, EvaluationContext evalContext) {
        return getExpression(this.keyCache, methodKey, keyExpression).getValue(evalContext);
    }

    public boolean condition(String conditionExpression, AnnotatedElementKey methodKey, EvaluationContext evalContext) {
        return (Boolean.TRUE.equals(getExpression(this.conditionCache, methodKey, conditionExpression)
                .getValue(evalContext, Boolean.class)));
    }

    public boolean unless(String unlessExpression, AnnotatedElementKey methodKey, EvaluationContext evalContext) {
        return (Boolean.TRUE.equals(
                getExpression(this.unlessCache, methodKey, unlessExpression).getValue(evalContext, Boolean.class)));
    }

    public Object eval(String expression, AnnotatedElementKey methodKey, EvaluationContext evalContext) {
        return getExpression(this.unlessCache, methodKey, expression).getValue(evalContext, Object.class);
    }

    /**
     * Clear all caches.
     */
    void clear() {
        this.keyCache.clear();
        this.conditionCache.clear();
        this.unlessCache.clear();
        this.evalCache.clear();
    }

}

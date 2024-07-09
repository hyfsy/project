package com.hyf.cache.impl.spel;

import java.lang.reflect.AnnotatedElement;

import org.springframework.context.expression.AnnotatedElementKey;
import org.springframework.expression.EvaluationContext;

import com.hyf.cache.bus.CacheResultResolver;

/**
 * @author baB_hyf
 * @date 2022/02/11
 */
public class SpELCacheResultResolver implements CacheResultResolver<Object>
{

    private EvaluationContext evaluationContext;
    private String el;
    private AnnotatedElement element;
    private Class<?> targetClass;

    public SpELCacheResultResolver(EvaluationContext evaluationContext, String el, Class<?> targetClass,
                                   AnnotatedElement element) {
        this.evaluationContext = evaluationContext;
        this.el = el;
        this.element = element;
        this.targetClass = targetClass;
    }

    @Override
    public Object convertCacheResult(Object[] params, Object cacheValue) {
        evaluationContext.setVariable("cacheValue", cacheValue);
        return EvaluationManager.eval(el, new AnnotatedElementKey(element, targetClass), evaluationContext);
    }
}

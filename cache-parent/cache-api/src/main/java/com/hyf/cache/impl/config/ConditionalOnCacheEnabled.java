package com.hyf.cache.impl.config;

import java.lang.annotation.*;

import com.hyf.cache.enums.CacheType;
import org.springframework.context.annotation.Conditional;

/**
 * @author baB_hyf
 * @date 2022/03/03
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE })
@Documented
@Conditional(CacheEnabledCondition.class)
public @interface ConditionalOnCacheEnabled
{

    CacheType[] value() default {};
}

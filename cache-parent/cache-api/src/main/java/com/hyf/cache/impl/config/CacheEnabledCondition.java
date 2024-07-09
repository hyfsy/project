package com.hyf.cache.impl.config;

import java.util.*;
import java.util.stream.Collectors;

import org.springframework.boot.autoconfigure.condition.ConditionMessage;
import org.springframework.boot.autoconfigure.condition.ConditionOutcome;
import org.springframework.boot.autoconfigure.condition.SpringBootCondition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.env.Environment;
import org.springframework.core.type.AnnotatedTypeMetadata;

import com.hyf.cache.constants.CacheConstants;
import com.hyf.cache.enums.CacheType;

/**
 * @author baB_hyf
 * @date 2022/03/03
 */
public class CacheEnabledCondition extends SpringBootCondition
{

    public static final String CACHE_PREFIX = CacheConstants.CACHE_PROPERTY_PREFIX;
    public static final String ENABLED = "enabled";
    public static final String DOT = ".";
    public static final Boolean MATCH_IF_MISSING = Boolean.TRUE;

    @Override
    public ConditionOutcome getMatchOutcome(ConditionContext context, AnnotatedTypeMetadata metadata) {

        Environment environment = context.getEnvironment();

        Map<String, Object> annotationAttributes = metadata
                .getAnnotationAttributes(ConditionalOnCacheEnabled.class.getName());
        if (annotationAttributes != null && annotationAttributes.containsKey("value")) {
            CacheType[] cacheTypes = (CacheType[]) annotationAttributes.get("value");

            List<String> properties = Arrays.stream(cacheTypes)
                    .map(t -> CACHE_PREFIX + DOT + t.name().toLowerCase(Locale.ROOT) + DOT + ENABLED)
                    .collect(Collectors.toList());
            properties.add(0, CACHE_PREFIX + DOT + ENABLED);

            Boolean cacheEnabled = true;
            String propertyName = "";
            Iterator<String> it = properties.iterator();
            while (it.hasNext() && cacheEnabled) {
                propertyName = it.next();
                cacheEnabled = environment.getProperty(propertyName, Boolean.class);
                cacheEnabled = cacheEnabled == null ? MATCH_IF_MISSING : cacheEnabled;
            }

            return new ConditionOutcome(cacheEnabled, ConditionMessage.forCondition(ConditionalOnCacheEnabled.class)
                    .because("found property " + propertyName + " with value " + cacheEnabled));
        }

        return ConditionOutcome.match(ConditionMessage.of("default matched"));
    }
}

package com.hyf.cache.impl.operation;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;

import org.springframework.cache.annotation.CacheAnnotationParser;
import org.springframework.cache.annotation.SpringCacheAnnotationParser;
import org.springframework.cache.interceptor.CacheOperation;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import com.hyf.cache.BaseCacheOperation;
import com.hyf.cache.annotation.*;
import com.hyf.cache.bus.annotation.ReuseCacheable;
import com.hyf.cache.bus.annotation.ReuseModifiable;
import com.hyf.cache.impl.annotation.CacheAnnotationUtils;
import com.hyf.cache.impl.constants.CacheKeyConstants;

/**
 * TODO 动态扩展
 *
 * 解析缓存相关注解的选项
 * 
 * @author baB_hyf
 * @date 2022/02/08
 * @see EpCacheable
 * @see EpCacheEvict
 * @see ReuseCacheable
 * @see ReuseModifiable
 * @see CacheableOperation
 * @see CacheEvictOperation
 * @see ReuseCacheableOperation
 * @see ReuseModifiableOperation
 */
public class CustomCacheAnnotationParser implements CacheAnnotationParser
{

    private final Set<Class<? extends Annotation>> cacheOperationAnnotations = getParseAnnotationSet();

    protected Set<Class<? extends Annotation>> getParseAnnotationSet() {
        Set<Class<? extends Annotation>> annotationSet = new LinkedHashSet<>(8);
        // TODO 支持注入其他的
        annotationSet.add(EpCacheable.class);
        annotationSet.add(EpCacheEvict.class);
        annotationSet.add(ReuseCacheable.class);
        annotationSet.add(ReuseModifiable.class);
        return annotationSet;
    }

    @Override
    public boolean isCandidateClass(Class<?> targetClass) {
        return AnnotationUtils.isCandidateClass(targetClass, cacheOperationAnnotations);
    }

    @Override
    public Collection<CacheOperation> parseCacheAnnotations(Class<?> type) {
        return parseCacheAnnotations((AnnotatedElement) type);
    }

    @Override
    public Collection<CacheOperation> parseCacheAnnotations(Method method) {
        return parseCacheAnnotations((AnnotatedElement) method);
    }

    protected Collection<CacheOperation> parseCacheAnnotations(AnnotatedElement ae) {
        Collection<CacheOperation> ops = parseCacheAnnotations(ae, false);
        if (ops != null && ops.size() > 1) {
            // More than one operation found -> local declarations override
            // interface-declared ones...
            Collection<CacheOperation> localOps = parseCacheAnnotations(ae, true);
            if (localOps != null) {
                return localOps;
            }
        }
        return ops;
    }

    protected Collection<CacheOperation> parseCacheAnnotations(AnnotatedElement ae, boolean localOnly) {

        Collection<? extends Annotation> anns = (localOnly
                ? AnnotatedElementUtils.getAllMergedAnnotations(ae, cacheOperationAnnotations)
                : AnnotatedElementUtils.findAllMergedAnnotations(ae, cacheOperationAnnotations));
        if (anns.isEmpty()) {
            return null;
        }

        final Collection<CacheOperation> ops = new ArrayList<>(1);
        anns.stream().filter(ann -> ann instanceof EpCacheable)
                .forEach(ann -> ops.add(parseCacheableAnnotation(ae, (EpCacheable) ann)));
        anns.stream().filter(ann -> ann instanceof EpCacheEvict)
                .forEach(ann -> ops.add(parseEvictAnnotation(ae, (EpCacheEvict) ann)));
        anns.stream().filter(ann -> ann instanceof ReuseCacheable)
                .forEach(ann -> ops.add(parseReuseCacheableAnnotation(ae, (ReuseCacheable) ann)));
        anns.stream().filter(ann -> ann instanceof ReuseModifiable)
                .forEach(ann -> ops.add(parseReuseModifiableAnnotation(ae, (ReuseModifiable) ann)));
        return ops;
    }

    protected CacheableOperation parseCacheableAnnotation(AnnotatedElement ae, EpCacheable cacheable) {

        CacheableOperation.Builder builder = new CacheableOperation.Builder();
        builder.setSync(cacheable.sync());
        builder.setForceConsistency(cacheable.forceConsistency());
        builder.setCacheType(cacheable.cacheType());
        builder.setStoreType(cacheable.storeType());
        builder.setDynamicCreate(cacheable.dynamicCreate());
        builder.setCacheResultConverter(cacheable.cacheResultConverter());

        basicBuild(builder, ae);

        builder.setType(CacheKeyConstants.TYPE_BUSINESS_METHOD);

        CacheableOperation op = builder.build();
        validateBaseCacheOperation(ae, op);

        return op;
    }

    protected CacheEvictOperation parseEvictAnnotation(AnnotatedElement ae, EpCacheEvict cacheEvict) {

        CacheEvictOperation.Builder builder = new CacheEvictOperation.Builder();
        builder.setCacheResolver(cacheEvict.cacheResolver());
        builder.setSync(cacheEvict.sync());
        builder.setCacheType(cacheEvict.cacheType());
        builder.setAllKeysNoSpecified(cacheEvict.allKeysNoSpecified());
        builder.setAllEntries(cacheEvict.allEntries());
        builder.setBeforeInvocation(cacheEvict.beforeInvocation());

        basicBuild(builder, ae);

        builder.setType(CacheKeyConstants.TYPE_BUSINESS_METHOD);

        CacheEvictOperation op = builder.build();
        validateBaseCacheOperation(ae, op);

        return op;
    }

    protected ReuseCacheableOperation parseReuseCacheableAnnotation(AnnotatedElement ae,
            ReuseCacheable reuseCacheable) {

        ReuseCacheableOperation.Builder builder = new ReuseCacheableOperation.Builder();
        builder.setMapperIdx(reuseCacheable.mapperIdx());
        builder.setCacheProperty(reuseCacheable.cacheProperty());

        builder.setCacheResolver(reuseCacheable.cacheResolver());
        builder.setForceConsistency(reuseCacheable.forceConsistency());
        builder.setCacheType(reuseCacheable.cacheType());
        builder.setStoreType(reuseCacheable.storeType());
        builder.setDynamicCreate(reuseCacheable.dynamicCreate());
        builder.setCacheResultResolver(reuseCacheable.cacheResultResolver());

        basicBuild(builder, ae);

        builder.setType(CacheKeyConstants.TYPE_BUSINESS_INDEX);
        builder.setTtl(-1); // no expire

        ReuseCacheableOperation op = builder.build();
        validateBaseCacheOperation(ae, op);

        return op;
    }

    protected ReuseModifiableOperation parseReuseModifiableAnnotation(AnnotatedElement ae,
            ReuseModifiable reuseModifiable) {

        ReuseModifiableOperation.Builder builder = new ReuseModifiableOperation.Builder();
        builder.setOp(reuseModifiable.op());
        builder.setMapperIdx(reuseModifiable.mapperIdx());
        builder.setReuseCacheModifier(reuseModifiable.reuseCacheModifier());

        builder.setCacheResolver(reuseModifiable.cacheResolver());
        builder.setForceConsistency(reuseModifiable.forceConsistency());
        builder.setCacheType(reuseModifiable.cacheType());
        builder.setBeforeInvocation(reuseModifiable.beforeInvocation());

        basicBuild(builder, ae);

        builder.setType(CacheKeyConstants.TYPE_BUSINESS_INDEX);
        builder.setTtl(-1); // no expire

        ReuseModifiableOperation op = builder.build();
        validateBaseCacheOperation(ae, op);

        return op;
    }

    protected void basicBuild(BaseCacheOperation.Builder builder, AnnotatedElement ae) {
        Assert.notNull(builder, "builder must not be null");
        Assert.notNull(ae, "AnnotatedElement must not be null");

        CacheKey cacheKey = CacheAnnotationUtils.getCacheKey(ae);
        CacheCondition cacheCondition = CacheAnnotationUtils.getCacheCondition(ae);
        CacheTTL cacheTTL = CacheAnnotationUtils.getCacheTTL(ae);

        builder.setName(ae.toString());

        if (cacheKey != null) {
            builder.setCacheNames(cacheKey.cacheNames());
            builder.setCacheClass(cacheKey.cacheClass());
            builder.setCacheClassName(cacheKey.cacheClassName());
            builder.setKey(cacheKey.key());
            builder.setKeyGenerator(cacheKey.keyGenerator());
        }

        if (cacheCondition != null) {
            builder.setCondition(cacheCondition.condition());
            builder.setUnless(cacheCondition.unless());
            builder.setConditionMethod(cacheCondition.conditionMethod());
            builder.setConditionArgs(cacheCondition.conditionArgs());
            builder.setUnlessMethod(cacheCondition.unlessMethod());
            builder.setUnlessArgs(cacheCondition.unlessArgs());
            builder.setAllowNull(cacheCondition.allowNull());
        }

        if (cacheTTL != null) {
            builder.setTtl(cacheTTL.ttl());
            builder.setUnit(cacheTTL.unit());
            builder.setRandom(cacheTTL.random());
        }
    }

    protected void validateBaseCacheOperation(AnnotatedElement ae, BaseCacheOperation operation) {

        long ttl = operation.getTtl();
        if (ttl == 0) {
            throw new IllegalStateException("ttl must not be zero: " + ae.toString());
        }

        // TODO
        if (StringUtils.hasText(operation.getKey()) && StringUtils.hasText(operation.getKeyGenerator())) {
            throw new IllegalStateException("Invalid cache annotation configuration on '" + ae.toString()
                    + "'. Both 'key' and 'keyGenerator' attributes have been set. "
                    + "These attributes are mutually exclusive: either set the SpEL expression used to"
                    + "compute the key at runtime or set the name of the KeyGenerator bean to use.");
        }
        if (StringUtils.hasText(operation.getCacheManager()) && StringUtils.hasText(operation.getCacheResolver())) {
            throw new IllegalStateException("Invalid cache annotation configuration on '" + ae.toString()
                    + "'. Both 'cacheManager' and 'cacheResolver' attributes have been set. "
                    + "These attributes are mutually exclusive: the cache manager is used to configure a"
                    + "default cache resolver if none is set. If a cache resolver is set, the cache manager"
                    + "won't be used.");
        }
    }

    @Override
    public boolean equals(Object other) {
        return (other instanceof SpringCacheAnnotationParser);
    }

    @Override
    public int hashCode() {
        return this.getClass().hashCode();
    }

}

package com.hyf.cache.impl.key.generator;

import java.lang.reflect.Method;
import java.util.*;

import org.springframework.context.expression.AnnotatedElementKey;
import org.springframework.expression.EvaluationContext;
import org.springframework.util.ClassUtils;

import com.hyf.cache.InvocationContext;
import com.hyf.cache.OperationBasedKeyGenerator;
import com.hyf.cache.impl.context.DefaultInvocationContext;
import com.hyf.cache.impl.index.bus.ReuseIndexCache;
import com.hyf.cache.impl.index.bus.ReuseIndexCacheImpl;
import com.hyf.cache.impl.key.AbstractCacheKey;
import com.hyf.cache.impl.key.CompositeCacheKey;
import com.hyf.cache.impl.key.ReuseBusinessCacheKey;
import com.hyf.cache.impl.operation.ReuseCacheableOperation;
import com.hyf.cache.impl.spel.EvaluationManager;
import com.hyf.cache.impl.utils.StringUtils;
import com.hyf.cache.impl.utils.TypeUtils;

/**
 * 可复用业务key生成器
 *
 * @author baB_hyf
 * @date 2022/02/09
 */
public class ReuseBusinessCacheKeyGenerator extends OperationBasedKeyGenerator<ReuseCacheableOperation>
{

    private ReuseIndexCache indexCache = new ReuseIndexCacheImpl();

    public ReuseBusinessCacheKeyGenerator(ReuseCacheableOperation operation) {
        super(operation);
    }

    @Override
    public Object generate(Object target, Method method, Object... params) {

        List<AbstractCacheKey> cacheKeyList = new ArrayList<>();
        String cacheClassName = operation.getCacheClassName();
        if (StringUtils.isBlank(cacheClassName)) {
            cacheClassName = operation.getCacheClass() == null ? null : operation.getCacheClass().getName();
        }

        String[] mapperIdx = operation.getMapperIdx();

        // 解析el表达式和主索引名称

        Class<?> cacheClass = getCacheClass(target, method, params);
        // default index
        String defaultIndexName = getIndexName(cacheClass);

        String indexName = defaultIndexName;
        String elKey = "";

        String aliasSplitString = ":";

        // 默认唯一索引
        if (mapperIdx.length == 0) {
            elKey = indexName;
        }
        // 指定唯一索引
        else if (mapperIdx.length == 1) {
            String indexString = mapperIdx[0];
            int aliasSplit = indexString.lastIndexOf(aliasSplitString);
            if (aliasSplit != -1) {
                elKey = indexString.substring(0, aliasSplit);
                indexName = indexString.substring(aliasSplit + aliasSplitString.length());
            }
            else {
                elKey = indexString;
                indexName = indexString.replaceAll("#", "");
            }
        }
        // 组合索引
        else {
            // TODO
        }

        List<String> pkList = new LinkedList<>();

        if ("".equals(elKey)) {
            // TODO 缓存中获取所有的key
            // Set<String> keys = cache.keys();
            // pkList.addAll(keys);
        }
        else {
            // 获取索引映射的结果集
            List<String> mapperValueList = new LinkedList<>();
            Object result = getResult(elKey, target, method, params);
            if (result.getClass().isAssignableFrom(Collection.class)) {
                // TODO check generic type only string type
                Collection<String> collection = (Collection<String>) result;
                mapperValueList.addAll(collection);
            }
            else {
                mapperValueList.add(result.toString());
            }

            // 通过结果集映射出实际的索引值列表
            if (indexName.equals(defaultIndexName)) {
                pkList.addAll(mapperValueList);
            }
            else {
                for (String mapperValue : mapperValueList) {
                    pkList.addAll(indexCache.getIndex(indexName, mapperValue));
                }
            }
        }

        // 构建所有的业务key
        Set<String> cacheNames = operation.getCacheNames();
        for (String cacheName : cacheNames) {
            for (String pk : pkList) {
                cacheKeyList.add(new ReuseBusinessCacheKey(cacheName, cacheClassName, pk));
            }
        }

        return new CompositeCacheKey(cacheKeyList);
    }

    public boolean matchEntity(Class<?> clazz) {
        // TODO
        return true;
    }

    public String getIndexName(Class<?> clazz) {
        // TODO
        String idxName = "rowGuid";
        if (matchEntity(clazz)) {
            // get from entity class annotation
        }

        if ("".equals(idxName)) {
            // get from CacheClassIndexMapper
        }

        if ("".equals(idxName)) {
            throw new IllegalStateException("Class cannot find index: " + clazz.getName());
        }

        return idxName;
    }

    public Object getResult(String key, Object target, Method method, Object... params) {

        InvocationContext invocationContext = DefaultInvocationContext.simple(target, method, params);
        EvaluationContext evaluationContext = EvaluationManager.createEvaluationContext(invocationContext);
        AnnotatedElementKey methodKey = new AnnotatedElementKey(invocationContext.getTargetMethod(),
                invocationContext.getTargetClass());
        Object result = EvaluationManager.key(key, methodKey, evaluationContext);

        // only support string
        // if (!(result instanceof String)) {
        // throw new IllegalArgumentException("key type must be string: " +
        // result.getClass().getName());
        // }

        return result;
    }

    public Class<?> getCacheClass(Object target, Method method, Object... params) {
        Class<?> cacheClass = operation.getCacheClass();
        if (cacheClass == null || cacheClass == Class.class) {
            if (!"".equals(operation.getCacheClassName())) {
                try {
                    cacheClass = ClassUtils.forName(operation.getCacheClassName(), null);
                }
                catch (ClassNotFoundException e) {
                    throw new RuntimeException("Generate key failed", e);
                }
            }
            // 方法返回值查找默认
            else {
                InvocationContext invocationContext = DefaultInvocationContext.simple(target, method, params);
                cacheClass = TypeUtils.getMethodActualReturnType(invocationContext.getMethod());
            }
        }

        return cacheClass;
    }
}

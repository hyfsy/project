package com.hyf.cache.impl.key.generator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.hyf.cache.BaseCacheOperation;
import com.hyf.cache.InvocationContext;
import com.hyf.cache.KeyGenerator;
import com.hyf.cache.impl.constants.CacheKeyConstants;
import com.hyf.cache.impl.key.AbstractCacheKey;
import com.hyf.cache.impl.operation.ReuseCacheableOperation;

/**
 * 统一的缓存key生成入口
 * 
 * @author baB_hyf
 * @date 2022/02/09
 */
public class KeyGenerateManager
{

    /**
     * 生成缓存key对象
     *
     * @param cacheName
     *            缓存名称
     * @param operation
     *            缓存选项
     * @param invocationContext
     *            执行上下文
     * @return 缓存key对象
     */
    public static Object generate(String cacheName, BaseCacheOperation operation, InvocationContext invocationContext) {
        return generate(cacheName, operation.getType(), operation, invocationContext);
    }

    /**
     * 生成缓存key对象
     *
     * @param cacheName
     *            缓存名称
     * @param type
     *            缓存类型
     * @param operation
     *            缓存选项
     * @param invocationContext
     *            执行上下文
     * @return 缓存key对象
     */
    public static Object generate(String cacheName, String type, BaseCacheOperation operation,
            InvocationContext invocationContext) {

        KeyGenerator keyGenerator;

        // TODO
        switch (type) {
            case CacheKeyConstants.TYPE_BUSINESS_REUSE:
                // TODO
                keyGenerator = new ReuseBusinessCacheKeyGenerator((ReuseCacheableOperation) operation);
                break;
            case CacheKeyConstants.TYPE_BUSINESS_METHOD:
                keyGenerator = new MethodBusinessCacheKeyGenerator(operation);
                break;
            case CacheKeyConstants.TYPE_INDEX_SPRING:
            case CacheKeyConstants.TYPE_INDEX_CUSTOM:
                keyGenerator = new IndexKeyGenerator(type, cacheName);
                break;
            case CacheKeyConstants.TYPE_INDEX_BUSINESS_REUSE:
            case CacheKeyConstants.TYPE_INDEX_BUSINESS_INDEX:
            case CacheKeyConstants.TYPE_INDEX_BUSINESS_METHOD:
                keyGenerator = new BusinessIndexKeyGenerator(operation, type, cacheName);
                break;
            default:
                throw new RuntimeException("unknown type: " + type);
        }

        return keyGenerator.generate(invocationContext.getTarget(), invocationContext.getMethod(),
                invocationContext.getArgs());
    }

    // TODO 修改为通过Builder构建
    @Deprecated
    public static List<Object> getAllCacheKeyPattern(String cacheName) {

        List<Object> keys = new ArrayList<>();

        List<String> typeList = getTypeList();

        for (String type : typeList) {
            AbstractCacheKey abstractCacheKey = new AbstractCacheKey(cacheName)
            {

                @Override
                protected String custom() {
                    return "*";
                }

                @Override
                public String getType() {
                    return type;
                }
            };
            keys.add(abstractCacheKey);
        }

        return keys;
    }

    public static List<String> getTypeList() {
        return Arrays.asList(CacheKeyConstants.TYPE_BUSINESS, CacheKeyConstants.TYPE_BUSINESS_REUSE,
                CacheKeyConstants.TYPE_BUSINESS_INDEX, CacheKeyConstants.TYPE_BUSINESS_METHOD,
                CacheKeyConstants.TYPE_INDEX, CacheKeyConstants.TYPE_INDEX_SPRING, CacheKeyConstants.TYPE_INDEX_CUSTOM,
                CacheKeyConstants.TYPE_INDEX_BUSINESS_REUSE, CacheKeyConstants.TYPE_INDEX_BUSINESS_INDEX,
                CacheKeyConstants.TYPE_INDEX_BUSINESS_METHOD);
    }

}

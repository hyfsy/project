package com.hyf.cache.impl.key.generator;

import java.lang.reflect.Method;

import com.hyf.cache.BaseCacheOperation;
import com.hyf.cache.OperationBasedKeyGenerator;
import com.hyf.cache.impl.constants.CacheKeyConstants;
import com.hyf.cache.impl.key.BusinessIndexCacheKey;
import com.hyf.cache.impl.operation.CacheEvictOperation;
import com.hyf.cache.impl.operation.CacheableOperation;
import com.hyf.cache.impl.operation.ReuseCacheableOperation;
import com.hyf.cache.impl.operation.ReuseModifiableOperation;

/**
 * 业务索引key生成器
 * 
 * @author baB_hyf
 * @date 2022/02/11
 */
public class BusinessIndexKeyGenerator extends OperationBasedKeyGenerator<BaseCacheOperation>
{

    private String type;
    private String cacheName;

    public BusinessIndexKeyGenerator(BaseCacheOperation operation, String type, String cacheName) {
        super(operation);
        this.type = type;
        this.cacheName = cacheName;
    }

    @Override
    public Object generate(Object target, Method method, Object... params) {
        String businessType = operation.getType();
        // 可复用缓存类型
        if (operation instanceof ReuseCacheableOperation || operation instanceof ReuseModifiableOperation) {
            businessType = CacheKeyConstants.TYPE_INDEX_BUSINESS_REUSE;
        }
        // 方法缓存类型
        else if (operation instanceof CacheableOperation || operation instanceof CacheEvictOperation) {
            businessType = CacheKeyConstants.TYPE_INDEX_BUSINESS_METHOD;
        }
        return new BusinessIndexCacheKey(cacheName, type, operation.getCacheClassName(), businessType);
    }
}

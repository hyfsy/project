package com.hyf.cache.impl.key;

import org.springframework.beans.BeanUtils;

import com.hyf.cache.impl.constants.CacheKeyConstants;

/**
 * class不需要，可以通过 businessClassName 区分 method不需要，因为对于操作实体缓存的情况，不能通过方法区分，也不需要
 *
 * @author baB_hyf
 * @date 2022/02/08
 */
public class MethodBusinessCacheKey extends AbstractBusinessCacheKey
{

    private final Object[] args;
    private final String id;

    public MethodBusinessCacheKey(String cacheName, String businessClassName, String id, Object... params) {
        super(cacheName, businessClassName, CacheKeyConstants.TYPE_BUSINESS_METHOD);
        this.args = params.clone();
        this.id = id;
        checkSimpleType(args);
    }

    public Object[] getArgs() {
        return args;
    }

    public String getId() {
        return id;
    }

    @Override
    protected String businessSegment() {
        return getId();
    }

    private void checkSimpleType(Object[] args) {
        for (Object arg : args) {
            if (!BeanUtils.isSimpleValueType(arg.getClass())) {
                // TODO class method
                throw new IllegalArgumentException("Illegal cache key args type: " + arg.getClass());
            }
        }
    }
}

package com.hyf.cache.impl.key.support;

/**
 * 缓存key的租户提供
 *
 * @author baB_hyf
 * @date 2022/02/08
 */
@Deprecated
public interface CacheTenantSupplier
{
    String getTenant();
}

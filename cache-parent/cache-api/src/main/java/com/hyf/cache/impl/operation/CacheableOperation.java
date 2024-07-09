package com.hyf.cache.impl.operation;

import com.hyf.cache.BaseCacheOperation;
import com.hyf.cache.enums.StoreType;

/**
 * {@link com.hyf.cache.annotation.EpCacheable}注解的选项配置
 * 
 * @author baB_hyf
 * @date 2022/02/08
 * @see com.hyf.cache.annotation.EpCacheable
 */
public class CacheableOperation extends BaseCacheOperation
{

    private final String cacheResolver;
    private final boolean sync;
    private final boolean forceConsistency;
    private final StoreType storeType;
    private final boolean dynamicCreate;
    private final String cacheResultConverter;

    /**
     * Create a new {@link CacheableOperation} instance from the given builder.
     *
     * @param b
     * @since 4.3
     */
    protected CacheableOperation(CacheableOperation.Builder b) {
        super(b);
        this.cacheResolver = b.cacheResolver;
        this.sync = b.sync;
        this.forceConsistency = b.forceConsistency;
        this.storeType = b.storeType;
        this.dynamicCreate = b.dynamicCreate;
        this.cacheResultConverter = b.cacheResultConverter;
    }

    @Override
    public String getCacheResolver() {
        return cacheResolver;
    }

    public boolean isSync() {
        return sync;
    }

    public boolean isForceConsistency() {
        return forceConsistency;
    }

    public StoreType getStoreType() {
        return storeType;
    }

    public boolean isDynamicCreate() {
        return dynamicCreate;
    }

    public String getCacheResultConverter() {
        return cacheResultConverter;
    }

    public static class Builder extends BaseCacheOperation.Builder
    {

        private String cacheResolver;
        private boolean sync;
        private boolean forceConsistency;
        private StoreType storeType;
        private boolean dynamicCreate;
        private String cacheResultConverter;

        @Override
        public void setCacheResolver(String cacheResolver) {
            this.cacheResolver = cacheResolver;
        }

        public void setSync(boolean sync) {
            this.sync = sync;
        }

        public void setForceConsistency(boolean forceConsistency) {
            this.forceConsistency = forceConsistency;
        }

        public void setStoreType(StoreType storeType) {
            this.storeType = storeType;
        }

        public void setDynamicCreate(boolean dynamicCreate) {
            this.dynamicCreate = dynamicCreate;
        }

        public void setCacheResultConverter(String cacheResultConverter) {
            this.cacheResultConverter = cacheResultConverter;
        }

        @Override
        protected StringBuilder getOperationDescription() {
            StringBuilder sb = super.getOperationDescription();
            sb.append(" | cacheResolver='");
            sb.append(this.cacheResolver);
            sb.append("'");
            sb.append(" | atomic='");
            sb.append(this.sync);
            sb.append("'");
            sb.append(" | forceConsistency='");
            sb.append(this.forceConsistency);
            sb.append("'");
            sb.append(" | storeType='");
            sb.append(this.storeType);
            sb.append("'");
            sb.append(" | dynamicCreate='");
            sb.append(this.dynamicCreate);
            sb.append("'");
            sb.append(" | cacheResultResolver='");
            sb.append(this.cacheResultConverter);
            sb.append("'");
            return sb;
        }

        @Override
        public CacheableOperation build() {
            return new CacheableOperation(this);
        }
    }
}

package com.hyf.cache.impl.operation;

import com.hyf.cache.BaseCacheOperation;
import com.hyf.cache.enums.StoreType;

import java.util.Arrays;

/**
 * {@link com.hyf.cache.bus.annotation.ReuseCacheable}注解的选项配置
 *
 * @author baB_hyf
 * @date 2022/02/08
 * @see com.hyf.cache.bus.annotation.ReuseCacheable
 */
public class ReuseCacheableOperation extends BaseCacheOperation
{

    private final String[] mapperIdx;
    private final String cacheProperty;

    private final String cacheResolver;
    private final boolean     forceConsistency;
    private final StoreType   storeType;
    private final boolean     dynamicCreate;
    private final String cacheResultResolver;

    /**
     * Create a new {@link ReuseCacheableOperation} instance from the given builder.
     *
     * @param b
     * @since 4.3
     */
    protected ReuseCacheableOperation(ReuseCacheableOperation.Builder b) {
        super(b);
        this.mapperIdx = b.mapperIdx;
        this.cacheProperty = b.cacheProperty;

        this.cacheResolver = b.cacheResolver;
        this.forceConsistency = b.forceConsistency;
        this.storeType = b.storeType;
        this.dynamicCreate = b.dynamicCreate;
        this.cacheResultResolver = b.cacheResultResolver;
    }

    public String[] getMapperIdx() {
        return mapperIdx;
    }

    public String getCacheProperty() {
        return cacheProperty;
    }

    @Override
    public String getCacheResolver() {
        return cacheResolver;
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

    public String getCacheResultResolver() {
        return cacheResultResolver;
    }

    public static class Builder extends BaseCacheOperation.Builder
    {

        private String[] mapperIdx;
        private String cacheProperty;

        private String cacheResolver;
        private boolean forceConsistency;
        private StoreType storeType;
        private boolean dynamicCreate;
        private String cacheResultResolver;

        public void setMapperIdx(String[] mapperIdx) {
            this.mapperIdx = mapperIdx;
        }

        public void setCacheProperty(String cacheProperty) {
            this.cacheProperty = cacheProperty;
        }

        @Override
        public void setCacheResolver(String cacheResolver) {
            this.cacheResolver = cacheResolver;
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

        public void setCacheResultResolver(String cacheResultResolver) {
            this.cacheResultResolver = cacheResultResolver;
        }

        @Override
        protected StringBuilder getOperationDescription() {
            StringBuilder sb = super.getOperationDescription();
            sb.append(" | mapperIdx='");
            sb.append(Arrays.toString(this.mapperIdx));
            sb.append("'");
            sb.append(" | cacheProperty='");
            sb.append(this.cacheProperty);
            sb.append("'");

            sb.append(" | cacheResolver='");
            sb.append(this.cacheResolver);
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
            sb.append(this.cacheResultResolver);
            sb.append("'");
            return sb;
        }

        @Override
        protected void appendConditionDescription(StringBuilder sb) {
        }

        @Override
        protected void appendTTLDescription(StringBuilder sb) {
        }

        @Override
        public ReuseCacheableOperation build() {
            return new ReuseCacheableOperation(this);
        }
    }
}

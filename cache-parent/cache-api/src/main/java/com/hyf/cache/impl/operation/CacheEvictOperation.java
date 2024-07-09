package com.hyf.cache.impl.operation;

import com.hyf.cache.BaseCacheOperation;

/**
 * {@link com.hyf.cache.annotation.EpCacheEvict}注解的选项配置
 *
 * @author baB_hyf
 * @date 2022/02/08
 * @see com.hyf.cache.annotation.EpCacheEvict
 */
public class CacheEvictOperation extends BaseCacheOperation
{

    private final String cacheResolver;
    private final boolean sync;
    private final boolean allKeysNoSpecified;
    private final boolean allEntries;
    private final boolean beforeInvocation;

    /**
     * Create a new {@link CacheEvictOperation} instance from the given builder.
     *
     * @param b
     * @since 4.3
     */
    protected CacheEvictOperation(CacheEvictOperation.Builder b) {
        super(b);
        this.cacheResolver = b.cacheResolver;
        this.sync = b.sync;
        this.allKeysNoSpecified = b.allKeysNoSpecified;
        this.allEntries = b.allEntries;
        this.beforeInvocation = b.beforeInvocation;
    }

    @Override
    public String getCacheResolver() {
        return cacheResolver;
    }

    public boolean isSync() {
        return sync;
    }

    public boolean isAllKeysNoSpecified() {
        return allKeysNoSpecified;
    }

    public boolean isAllEntries() {
        return allEntries;
    }

    public boolean isBeforeInvocation() {
        return beforeInvocation;
    }

    public static class Builder extends BaseCacheOperation.Builder
    {

        private String cacheResolver;
        private boolean sync;
        private boolean allKeysNoSpecified;
        private boolean allEntries;
        private boolean beforeInvocation;

        @Override
        public void setCacheResolver(String cacheResolver) {
            this.cacheResolver = cacheResolver;
        }

        public void setSync(boolean sync) {
            this.sync = sync;
        }

        public void setAllKeysNoSpecified(boolean allKeysNoSpecified) {
            this.allKeysNoSpecified = allKeysNoSpecified;
        }

        public void setAllEntries(boolean allEntries) {
            this.allEntries = allEntries;
        }

        public void setBeforeInvocation(boolean beforeInvocation) {
            this.beforeInvocation = beforeInvocation;
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
            sb.append(" | allKeysNoSpecified='");
            sb.append(this.allKeysNoSpecified);
            sb.append("'");
            sb.append(" | allEntries='");
            sb.append(this.allEntries);
            sb.append("'");
            sb.append(" | beforeInvocation='");
            sb.append(this.beforeInvocation);
            sb.append("'");
            return sb;
        }

        @Override
        public CacheEvictOperation build() {
            return new CacheEvictOperation(this);
        }
    }
}

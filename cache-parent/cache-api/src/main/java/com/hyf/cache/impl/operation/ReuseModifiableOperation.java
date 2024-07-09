package com.hyf.cache.impl.operation;

import java.util.Arrays;

import com.hyf.cache.BaseCacheOperation;
import com.hyf.cache.enums.ModifyOperation;

/**
 * {@link com.hyf.cache.bus.annotation.ReuseModifiable}注解的选项配置
 *
 * @author baB_hyf
 * @date 2022/02/08
 * @see com.hyf.cache.bus.annotation.ReuseModifiable
 */
public class ReuseModifiableOperation extends BaseCacheOperation
{

    private final ModifyOperation op;
    private final String[]        mapperIdx;
    private final String reuseCacheModifier;

    private final String cacheResolver;
    private final boolean     forceConsistency;
    private final boolean     beforeInvocation;

    /**
     * Create a new {@link ReuseModifiableOperation} instance from the given
     * builder.
     *
     * @param b
     * @since 4.3
     */
    protected ReuseModifiableOperation(ReuseModifiableOperation.Builder b) {
        super(b);
        this.op = b.op;
        this.mapperIdx = b.mapperIdx;
        this.reuseCacheModifier = b.reuseCacheModifier;

        this.cacheResolver = b.cacheResolver;
        this.forceConsistency = b.forceConsistency;
        this.beforeInvocation = b.beforeInvocation;
    }

    public ModifyOperation getOp() {
        return op;
    }

    public String[] getMapperIdx() {
        return mapperIdx;
    }

    public String getReuseCacheModifier() {
        return reuseCacheModifier;
    }

    @Override
    public String getCacheResolver() {
        return cacheResolver;
    }

    public boolean isForceConsistency() {
        return forceConsistency;
    }

    public boolean isBeforeInvocation() {
        return beforeInvocation;
    }

    public static class Builder extends BaseCacheOperation.Builder
    {

        private ModifyOperation op;
        private String[]        mapperIdx;
        private String reuseCacheModifier;

        private String cacheResolver;
        private boolean     forceConsistency;
        private boolean beforeInvocation;

        public void setOp(ModifyOperation op) {
            this.op = op;
        }

        public void setMapperIdx(String[] mapperIdx) {
            this.mapperIdx = mapperIdx;
        }

        public void setReuseCacheModifier(String reuseCacheModifier) {
            this.reuseCacheModifier = reuseCacheModifier;
        }

        @Override
        public void setCacheResolver(String cacheResolver) {
            this.cacheResolver = cacheResolver;
        }

        public void setForceConsistency(boolean forceConsistency) {
            this.forceConsistency = forceConsistency;
        }

        public void setBeforeInvocation(boolean beforeInvocation) {
            this.beforeInvocation = beforeInvocation;
        }

        @Override
        protected StringBuilder getOperationDescription() {
            StringBuilder sb = super.getOperationDescription();
            sb.append(" | op='");
            sb.append(this.op);
            sb.append("'");
            sb.append(" | mapperIdx='");
            sb.append(Arrays.toString(this.mapperIdx));
            sb.append("'");
            sb.append(" | reuseCacheModifier='");
            sb.append(this.reuseCacheModifier);
            sb.append("'");

            sb.append(" | cacheResolver='");
            sb.append(this.cacheResolver);
            sb.append("'");
            sb.append(" | forceConsistency='");
            sb.append(this.forceConsistency);
            sb.append("'");
            sb.append(" | beforeInvocation='");
            sb.append(this.beforeInvocation);
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
        public ReuseModifiableOperation build() {
            return new ReuseModifiableOperation(this);
        }
    }
}

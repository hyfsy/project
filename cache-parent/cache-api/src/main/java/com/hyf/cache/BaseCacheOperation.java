package com.hyf.cache;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import org.springframework.cache.interceptor.CacheOperation;
import org.springframework.util.ClassUtils;

import com.hyf.cache.enums.CacheType;
import com.hyf.cache.impl.utils.StringUtils;

/**
 * 基本的缓存属性
 *
 * @author baB_hyf
 * @date 2022/02/09
 * @see com.hyf.cache.annotation.CacheKey
 * @see com.hyf.cache.annotation.CacheCondition
 * @see com.hyf.cache.annotation.CacheTTL
 */
public class BaseCacheOperation extends CacheOperation
{

    // @CacheKey
    // private final String name;
    // private final Set<String> cacheNames;
    private final Class<?> cacheClass;
    private final String cacheClassName;
    // private final String key;
    // private final String keyGenerator;

    // @CacheCondition
    // private final String condition;
    private final String unless;
    private final String conditionMethod;
    private final String conditionArgs;
    private final String unlessMethod;
    private final String unlessArgs;
    private final boolean allowNull;

    // @CacheTTL
    private final long ttl;
    private final TimeUnit unit;
    private final boolean random;

    // Cache common
    // private final String cacheResolver;
    private final CacheType[] cacheType;
    private String type;

    /**
     * Create a new {@link BaseCacheOperation} instance from the given builder.
     *
     * @param b
     * @since 4.3
     */
    protected BaseCacheOperation(BaseCacheOperation.Builder b) {
        super(b);
        this.cacheClass = b.cacheClass;
        this.cacheClassName = b.cacheClassName;
        this.unless = b.unless;
        this.conditionMethod = b.conditionMethod;
        this.conditionArgs = b.conditionArgs;
        this.unlessMethod = b.unlessMethod;
        this.unlessArgs = b.unlessArgs;
        this.allowNull = b.allowNull;
        this.ttl = b.ttl;
        this.unit = b.unit;
        this.random = b.random;
        this.type = b.type;
        this.cacheType = b.cacheType;
    }

    public Class<?> getCacheClass() {
        if (cacheClass == null) {
            try {
                return ClassUtils.forName(getCacheClassName(), null);
            }
            catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        return cacheClass;
    }

    public String getCacheClassName() {
        if (StringUtils.isBlank(cacheClassName)) {
            return getCacheClass().getName();
        }
        return cacheClassName;
    }

    public String getUnless() {
        return unless;
    }

    public String getConditionMethod() {
        return conditionMethod;
    }

    public String getConditionArgs() {
        return conditionArgs;
    }

    public String getUnlessMethod() {
        return unlessMethod;
    }

    public String getUnlessArgs() {
        return unlessArgs;
    }

    public boolean isAllowNull() {
        return allowNull;
    }

    public long getTtl() {
        return ttl;
    }

    public TimeUnit getUnit() {
        return unit;
    }

    public boolean isRandom() {
        return random;
    }

    public String getType() {
        return type;
    }

    public CacheType[] getCacheType() {
        return cacheType;
    }

    @Override
    public boolean equals(Object o) {

        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        if (!super.equals(o)) {
            return false;
        }
        BaseCacheOperation that = (BaseCacheOperation) o;
        return this.toString().equals(that.toString());
    }

    @Override
    public int hashCode() {
        return this.toString().hashCode();
    }

    public static class Builder extends CacheOperation.Builder
    {

        private Class<?> cacheClass;
        private String cacheClassName;
        private String unless;
        private String conditionMethod;
        private String conditionArgs;
        private String unlessMethod;
        private String unlessArgs;
        private boolean allowNull;
        private long ttl;
        private TimeUnit unit;
        private boolean random;
        private String type;
        private CacheType[] cacheType;

        public void setCacheClass(Class<?> cacheClass) {
            this.cacheClass = cacheClass;
        }

        public void setCacheClassName(String cacheClassName) {
            this.cacheClassName = cacheClassName;
        }

        public void setUnless(String unless) {
            this.unless = unless;
        }

        public void setConditionMethod(String conditionMethod) {
            this.conditionMethod = conditionMethod;
        }

        public void setConditionArgs(String conditionArgs) {
            this.conditionArgs = conditionArgs;
        }

        public void setUnlessMethod(String unlessMethod) {
            this.unlessMethod = unlessMethod;
        }

        public void setUnlessArgs(String unlessArgs) {
            this.unlessArgs = unlessArgs;
        }

        public void setAllowNull(boolean allowNull) {
            this.allowNull = allowNull;
        }

        public void setTtl(long ttl) {
            this.ttl = ttl;
        }

        public void setUnit(TimeUnit unit) {
            this.unit = unit;
        }

        public void setRandom(boolean random) {
            this.random = random;
        }

        public void setCacheType(CacheType[] cacheType) {
            this.cacheType = cacheType;
        }

        public void setType(String type) {
            this.type = type;
        }

        @Override
        protected StringBuilder getOperationDescription() {
            StringBuilder sb = super.getOperationDescription();
            appendKeyDescription(sb);
            appendConditionDescription(sb);
            appendTTLDescription(sb);
            appendExtendDescription(sb);
            return sb;
        }

        protected void appendKeyDescription(StringBuilder sb) {
            sb.append(" | cacheClass='");
            sb.append(this.cacheClass);
            sb.append("'");
            sb.append(" | cacheClassName='");
            sb.append(this.cacheClassName);
            sb.append("'");
        }

        protected void appendConditionDescription(StringBuilder sb) {
            sb.append(" | unless='");
            sb.append(this.unless);
            sb.append("'");
            sb.append(" | conditionMethod='");
            sb.append(this.conditionMethod);
            sb.append("'");
            sb.append(" | conditionArgs='");
            sb.append(this.conditionArgs);
            sb.append("'");
            sb.append(" | unlessMethod='");
            sb.append(this.unlessMethod);
            sb.append("'");
            sb.append(" | unlessArgs='");
            sb.append(this.unlessArgs);
            sb.append("'");
            sb.append(" | allowNull='");
            sb.append(this.allowNull);
            sb.append("'");
        }

        protected void appendTTLDescription(StringBuilder sb) {
            sb.append(" | ttl='");
            sb.append(this.ttl);
            sb.append("'");
            sb.append(" | unit='");
            sb.append(this.unit);
            sb.append("'");
            sb.append(" | random='");
            sb.append(this.random);
            sb.append("'");
        }

        protected void appendExtendDescription(StringBuilder sb) {
            sb.append(" | cacheType='");
            sb.append(Arrays.toString(this.cacheType));
            sb.append("'");
        }

        @Override
        public BaseCacheOperation build() {
            return new BaseCacheOperation(this);
        }
    }
}

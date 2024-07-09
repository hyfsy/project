package com.hyf.cache.impl.key.generator;

import java.util.ArrayList;
import java.util.List;

import com.hyf.cache.CacheKey;
import com.hyf.cache.impl.constants.CacheKeyConstants;
import com.hyf.cache.impl.key.CacheKeyUtils;
import com.hyf.cache.impl.key.StringCacheKey;
import com.hyf.cache.impl.utils.StringUtils;

/**
 * 缓存key构建器，生成{@link StringCacheKey}
 *
 * @author baB_hyf
 * @date 2022/02/18
 * @deprecated use CacheKeyBuilder2
 */
@Deprecated
public class CacheKeyBuilder
{

    public static Builder newBuilder(String cacheName) {
        return new Builder(cacheName);
    }

    public static class Builder
    {

        protected String version = CacheKeyUtils.getVersion();
        protected String app = CacheKeyUtils.getApplication();
        protected String tenant = CacheKeyUtils.getTenant();
        protected String cacheName = "";
        protected String suffix = "";

        protected List<String> segments = new ArrayList<>();

        public Builder(Builder builder) {
            this.version = builder.version;
            this.app = builder.app;
            this.tenant = builder.tenant;
            this.cacheName = builder.cacheName;
            this.suffix = builder.suffix;
            this.segments = builder.segments;
        }

        public Builder(String cacheName) {
            this.cacheName = cacheName;
        }

        public IndexBuilder index() {
            return new IndexBuilder(this);
        }

        public CacheKey spring(String signature) {
            append(CacheKeyConstants.TYPE_SPRING);
            append(signature);
            return build();
        }

        public BusinessBuilder business(String cacheClassName) {
            return new BusinessBuilder(this, cacheClassName);
        }

        public CacheKey custom(String custom) {
            append(CacheKeyConstants.TYPE_CUSTOM);
            append(custom);
            return build();
        }

        public Builder version(String version) {
            this.version = version;
            return this;
        }

        public Builder app(String app) {
            this.app = app;
            return this;
        }

        public Builder tenant(String tenant) {
            this.tenant = tenant;
            return this;
        }

        public Builder suffix(String suffix) {
            this.suffix = suffix;
            return this;
        }

        protected Builder append(String segment) {
            segments.add(segment);
            return this;
        }

        public CacheKey build() {
            segments.add(0, version);
            segments.add(1, app);
            segments.add(2, tenant);
            segments.add(3, cacheName);
            if (StringUtils.isBlank(segments.get(segments.size() - 1))) {
                segments.remove(segments.size() - 1);
            }
            return new StringCacheKey(String.join(CacheKeyConstants.KEY_SEPARATOR, segments) + suffix);
        }
    }

    public static class IndexBuilder extends Builder
    {

        public IndexBuilder(Builder builder) {
            super(builder);
            append(CacheKeyConstants.TYPE_INDEX);
        }

        public CacheKey springIndex() {
            append(CacheKeyConstants.TYPE_INDEX_SPRING);
            return build();
        }

        public CacheKey businessReuseIndex(String cacheClassName) {
            append(CacheKeyConstants.TYPE_INDEX_BUSINESS);
            append(cacheClassName);
            append(CacheKeyConstants.TYPE_INDEX_BUSINESS_REUSE);
            return build();
        }

        public CacheKey businessIndexIndex(String cacheClassName) {
            append(CacheKeyConstants.TYPE_INDEX_BUSINESS);
            append(cacheClassName);
            append(CacheKeyConstants.TYPE_INDEX_BUSINESS_INDEX);
            return build();
        }

        public CacheKey businessMethodIndex(String cacheClassName) {
            append(CacheKeyConstants.TYPE_INDEX_BUSINESS);
            append(cacheClassName);
            append(CacheKeyConstants.TYPE_INDEX_BUSINESS_METHOD);
            return build();
        }

        public CacheKey customIndex() {
            append(CacheKeyConstants.TYPE_INDEX_CUSTOM);
            return build();
        }

        public static class BusinessIndexBuilder extends Builder {

            public BusinessIndexBuilder(Builder builder) {
                super(builder);
            }
        }
    }

    public static class BusinessBuilder extends Builder
    {

        protected String cacheClassName = "";

        public BusinessBuilder(Builder builder) {
            super(builder);
            if (builder instanceof BusinessBuilder) {
                this.cacheClassName = ((BusinessBuilder) builder).cacheClassName;
            }
        }

        public BusinessBuilder(Builder builder, String cacheClassName) {
            super(builder);
            this.cacheClassName = cacheClassName;
        }

        public BusinessReuseBuilder reuse() {
            return new BusinessReuseBuilder(this);
        }

        public BusinessReuseIndexBuilder reuseIndex() {
            return new BusinessReuseIndexBuilder(this);
        }

        public BusinessMethodBuilder method() {
            return new BusinessMethodBuilder(this);
        }

        @Override
        public CacheKey build() {
            segments.add(0, CacheKeyConstants.TYPE_BUSINESS);
            segments.add(1, cacheClassName);
            return super.build();
        }

        public static class BusinessReuseBuilder extends BusinessBuilder
        {

            protected String guid;

            public BusinessReuseBuilder(Builder builder) {
                super(builder);
            }

            public BusinessReuseBuilder guid(String guid) {
                this.guid = guid;
                return this;
            }

            @Override
            public CacheKey build() {
                segments.add(0, CacheKeyConstants.TYPE_BUSINESS_REUSE);
                segments.add(1, guid);
                return super.build();
            }
        }

        public static class BusinessReuseIndexBuilder extends BusinessBuilder
        {
            protected String fieldName = "";
            protected String fieldValue = "";
            protected String guid = "";

            public BusinessReuseIndexBuilder(Builder builder) {
                super(builder);
            }

            public BusinessReuseIndexBuilder fieldName(String fieldName) {
                this.fieldName = fieldName;
                return this;
            }

            public BusinessReuseIndexBuilder fieldValue(String fieldValue) {
                this.fieldValue = fieldValue;
                return this;
            }

            public BusinessReuseIndexBuilder guid(String guid) {
                this.guid = guid;
                return this;
            }

            @Override
            public CacheKey build() {
                segments.add(0, CacheKeyConstants.TYPE_BUSINESS_INDEX);
                segments.add(1, String.join(CacheKeyConstants.KEY_SEGMENT_SEPARATOR, fieldName, fieldValue, guid));
                return super.build();
            }
        }

        public static class BusinessMethodBuilder extends BusinessBuilder
        {

            protected String guid = "";
            protected String signature = "";

            public BusinessMethodBuilder(BusinessBuilder builder) {
                super(builder);
            }

            public BusinessMethodBuilder guid(String guid) {
                this.guid = guid;
                return this;
            }

            public BusinessMethodBuilder signature(String signature) {
                this.signature = signature;
                return this;
            }

            @Override
            public CacheKey build() {
                segments.add(0, CacheKeyConstants.TYPE_BUSINESS_METHOD);
                segments.add(1, guid);
                segments.add(2, signature);
                return super.build();
            }
        }
    }
}

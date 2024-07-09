package com.hyf.cache.impl.key.generator;

import com.hyf.cache.CacheKey;
import com.hyf.cache.impl.constants.CacheKeyConstants;
import com.hyf.cache.impl.key.CacheKeyUtils;
import com.hyf.cache.impl.key.StringCacheKey;

/**
 * 缓存key构建器，生成{@link StringCacheKey}，指定通配符
 *
 * @author baB_hyf
 * @date 2022/02/18
 * @see StringCacheKey
 */
public class CacheKeyBuilder2
{

    public static IndexBuilder index(String cacheName) {
        return new IndexBuilder(cacheName);
    }

    public static SpringBuilder spring(String cacheName) {
        return new SpringBuilder(cacheName);
    }

    public static BusinessBuilder business(String cacheName) {
        return new BusinessBuilder(cacheName);
    }

    public static CustomBuilder custom(String cacheName) {
        return new CustomBuilder(cacheName);
    }

    public static DefaultBuilder newBuilder(String cacheName) {
        return new DefaultBuilder(cacheName);
    }

    private static String join(CharSequence delimiter, CharSequence... elements) {
        int lastEmptyIdx = elements.length - 1;
        while (lastEmptyIdx >= 0) {

            if (elements[lastEmptyIdx] != null && !"".contentEquals(elements[lastEmptyIdx])) {
                break;
            }

            lastEmptyIdx--;
        }

        int curLen = lastEmptyIdx + 1;
        if (elements.length != curLen) {
            CharSequence[] charSequences = new CharSequence[curLen];
            System.arraycopy(elements, 0, charSequences, 0, curLen);
            elements = charSequences;
        }

        for (int i = 0; i < elements.length; i++) {
            if (elements[i] == null) {
                elements[i] = "";
            }
        }

        return String.join(delimiter, elements);
    }

    private interface Buildable<B>
    {

        B build();

    }

    private interface Copyable<C extends Builder<C>>
    {
        void copy(C builder);
    }

    public static class DefaultBuilder extends StringCacheKeyBuilder<DefaultBuilder>
    {

        public DefaultBuilder(String cacheName) {
            super(cacheName);
        }

        @Override
        protected String buildString() {
            return join(CacheKeyConstants.KEY_SEPARATOR, version, type, app, tenant, cacheName) + suffix;
        }

        @Override
        protected DefaultBuilder getThis() {
            return this;
        }
    }

    public static class SpringBuilder extends StringCacheKeyBuilder<SpringBuilder>
    {

        protected String signature;

        public SpringBuilder(String cacheName) {
            super(cacheName);
            type(CacheKeyConstants.TYPE_SPRING);
        }

        public SpringBuilder signature(String signature) {
            this.signature = signature;
            return getThis();
        }

        @Override
        protected SpringBuilder getThis() {
            return this;
        }

        @Override
        protected String buildString() {
            return join(CacheKeyConstants.KEY_SEPARATOR, version, type, app, tenant, cacheName, CacheKeyConstants.TYPE_SPRING, signature) + suffix;
        }
    }

    public static class CustomBuilder extends StringCacheKeyBuilder<CustomBuilder>
    {

        protected String custom;

        public CustomBuilder(String cacheName) {
            super(cacheName);
            type(CacheKeyConstants.TYPE_CUSTOM);
        }

        public CustomBuilder customString(String custom) {
            this.custom = custom;
            return getThis();
        }

        @Override
        protected CustomBuilder getThis() {
            return this;
        }

        @Override
        protected String buildString() {
            return join(CacheKeyConstants.KEY_SEPARATOR, version, type, app, tenant, cacheName, CacheKeyConstants.TYPE_CUSTOM, custom) + suffix;
        }
    }

    private static abstract class StringCacheKeyBuilder<B extends Builder<B>> extends Builder<B>
    {

        public StringCacheKeyBuilder(String cacheName) {
            super(cacheName);
        }

        @Override
        public CacheKey build() {
            return new StringCacheKey(buildString());
        }

        protected abstract String buildString();
    }

    private static abstract class Builder<B extends Builder<B>> implements Buildable<CacheKey>
    {

        protected String version = CacheKeyUtils.getVersion();
        protected String type;
        protected String app = CacheKeyUtils.getApplication();
        protected String tenant = CacheKeyUtils.getTenant();
        protected String cacheName;
        protected String suffix = "";

        public Builder(String cacheName) {
            this.cacheName = cacheName;
        }

        public B version(String version) {
            this.version = version;
            return getThis();
        }

        public B type(String type) {
            this.type = type;
            return getThis();
        }

        public B app(String app) {
            this.app = app;
            return getThis();
        }

        public B tenant(String tenant) {
            this.tenant = tenant;
            return getThis();
        }

        public B suffix(String suffix) {
            this.suffix = suffix;
            return getThis();
        }

        protected abstract B getThis();

    }

    public static class IndexBuilder extends StringCacheKeyBuilder<IndexBuilder>
    {

        public IndexBuilder(String cacheName) {
            super(cacheName);
            type(CacheKeyConstants.TYPE_INDEX);
        }

        public IndexSpringBuilder spring() {
            return new IndexSpringBuilder(this);
        }

        public IndexBusinessBuilder business() {
            return new IndexBusinessBuilder(this);
        }

        public IndexCustomBuilder custom() {
            return new IndexCustomBuilder(this);
        }

        @Override
        protected String buildString() {
            return join(CacheKeyConstants.KEY_SEPARATOR, version, type, app, tenant, cacheName, CacheKeyConstants.TYPE_INDEX) + suffix;
        }

        @Override
        protected IndexBuilder getThis() {
            return this;
        }

    }

    public static class IndexBusinessBuilder extends IndexCopyableBuilder<IndexBuilder, IndexBusinessBuilder>
    {

        protected String cacheClassName;

        public IndexBusinessBuilder(IndexBuilder builder) {
            super(builder);
            type(CacheKeyConstants.TYPE_INDEX_BUSINESS);
        }

        public IndexBusinessBuilder cacheClassName(String cacheClassName) {
            this.cacheClassName = cacheClassName;
            return getThis();
        }

        public IndexBusinessReuseBuilder reuse() {
            return new IndexBusinessReuseBuilder(this);
        }

        public IndexBusinessIndexBuilder index() {
            return new IndexBusinessIndexBuilder(this);
        }

        public IndexBusinessMethodBuilder method() {
            return new IndexBusinessMethodBuilder(this);
        }

        @Override
        protected String buildString() {
            return join(CacheKeyConstants.KEY_SEPARATOR, version, type, app, tenant, cacheName, CacheKeyConstants.TYPE_INDEX, CacheKeyConstants.TYPE_BUSINESS, cacheClassName) + suffix;
        }

        @Override
        protected IndexBusinessBuilder getThis() {
            return this;
        }
    }

    public static class IndexBusinessReuseBuilder extends IndexBusinessCopyableBuilder<IndexBusinessReuseBuilder>
    {

        public IndexBusinessReuseBuilder(IndexBusinessBuilder builder) {
            super(builder);
            type(CacheKeyConstants.TYPE_INDEX_BUSINESS_REUSE);
        }

        @Override
        protected String buildString() {
            return join(CacheKeyConstants.KEY_SEPARATOR, version, type, app, tenant, cacheName, CacheKeyConstants.TYPE_INDEX, CacheKeyConstants.TYPE_BUSINESS, cacheClassName, CacheKeyConstants.TYPE_BUSINESS_REUSE) + suffix;
        }

        @Override
        protected IndexBusinessReuseBuilder getThis() {
            return this;
        }
    }

    public static class IndexBusinessIndexBuilder extends IndexBusinessCopyableBuilder<IndexBusinessIndexBuilder>
    {

        public IndexBusinessIndexBuilder(IndexBusinessBuilder builder) {
            super(builder);
            type(CacheKeyConstants.TYPE_INDEX_BUSINESS_INDEX);
        }

        @Override
        protected String buildString() {
            return join(CacheKeyConstants.KEY_SEPARATOR, version, type, app, tenant, cacheName, CacheKeyConstants.TYPE_INDEX, CacheKeyConstants.TYPE_BUSINESS, cacheClassName, CacheKeyConstants.TYPE_INDEX) + suffix;
        }

        @Override
        protected IndexBusinessIndexBuilder getThis() {
            return this;
        }
    }

    public static class IndexBusinessMethodBuilder extends IndexBusinessCopyableBuilder<IndexBusinessMethodBuilder>
    {

        public IndexBusinessMethodBuilder(IndexBusinessBuilder builder) {
            super(builder);
            type(CacheKeyConstants.TYPE_INDEX_BUSINESS_METHOD);
        }

        @Override
        protected String buildString() {
            return join(CacheKeyConstants.KEY_SEPARATOR, version, type, app, tenant, cacheName, CacheKeyConstants.TYPE_INDEX, CacheKeyConstants.TYPE_BUSINESS, cacheClassName, CacheKeyConstants.TYPE_BUSINESS_METHOD) + suffix;
        }

        @Override
        protected IndexBusinessMethodBuilder getThis() {
            return this;
        }
    }

    private static abstract class IndexBusinessCopyableBuilder<B extends Builder<B>> extends IndexCopyableBuilder<IndexBusinessBuilder, B>
    {

        protected String cacheClassName;

        public IndexBusinessCopyableBuilder(IndexBusinessBuilder builder) {
            super(builder);
        }

        public B cacheClassName(String cacheClassName) {
            this.cacheClassName = cacheClassName;
            return getThis();
        }

        @Override
        public void copy(IndexBusinessBuilder builder) {
            super.copy(builder);
            this.cacheClassName = builder.cacheClassName;
        }
    }

    public static class IndexSpringBuilder extends IndexCopyableBuilder<IndexBuilder, IndexSpringBuilder>
    {

        public IndexSpringBuilder(IndexBuilder builder) {
            super(builder);
            type(CacheKeyConstants.TYPE_INDEX_SPRING);
        }

        @Override
        protected String buildString() {
            return join(CacheKeyConstants.KEY_SEPARATOR, version, type, app, tenant, cacheName, CacheKeyConstants.TYPE_INDEX, CacheKeyConstants.TYPE_SPRING) + suffix;
        }

        @Override
        protected IndexSpringBuilder getThis() {
            return this;
        }
    }

    public static class IndexCustomBuilder extends IndexCopyableBuilder<IndexBuilder, IndexCustomBuilder>
    {

        public IndexCustomBuilder(IndexBuilder builder) {
            super(builder);
            type(CacheKeyConstants.TYPE_INDEX_CUSTOM);
        }

        @Override
        protected String buildString() {
            return join(CacheKeyConstants.KEY_SEPARATOR, version, type, app, tenant, cacheName, CacheKeyConstants.TYPE_INDEX, CacheKeyConstants.TYPE_CUSTOM) + suffix;
        }

        @Override
        protected IndexCustomBuilder getThis() {
            return this;
        }
    }

    public static class BusinessBuilder extends StringCacheKeyBuilder<BusinessBuilder>
    {

        protected String cacheClassName;

        public BusinessBuilder(String cacheName) {
            super(cacheName);
            type(CacheKeyConstants.TYPE_BUSINESS);
        }

        public BusinessBuilder cacheClassName(String cacheClassName) {
            this.cacheClassName = cacheClassName;
            return getThis();
        }

        public BusinessReuseBuilder reuse() {
            return new BusinessReuseBuilder(this);
        }

        public BusinessIndexBuilder index() {
            return new BusinessIndexBuilder(this);
        }

        public BusinessMethodBuilder method() {
            return new BusinessMethodBuilder(this);
        }

        @Override
        protected BusinessBuilder getThis() {
            return this;
        }

        @Override
        protected String buildString() {
            return join(CacheKeyConstants.KEY_SEPARATOR, version, type, app, tenant, cacheName, CacheKeyConstants.TYPE_BUSINESS, cacheClassName) + suffix;
        }
    }

    private static abstract class IndexCopyableBuilder<C extends Builder<C>, B extends Builder<B>> extends StringCacheKeyBuilder<B> implements Copyable<C>
    {

        public IndexCopyableBuilder(C builder) {
            super(builder.cacheName);
            copy(builder);
        }

        @Override
        public void copy(C builder) {
            this.version = builder.version;
            this.type = builder.type;
            this.app = builder.app;
            this.tenant = builder.tenant;
            this.cacheName = builder.cacheName;
            this.suffix = builder.suffix;
        }
    }

    private static abstract class BusinessCopyableBuilder<B extends Builder<B>> extends StringCacheKeyBuilder<B> implements Copyable<BusinessBuilder>
    {

        protected String cacheClassName;

        public BusinessCopyableBuilder(BusinessBuilder builder) {
            super(builder.cacheName);
            copy(builder);
        }

        public B cacheClassName(String cacheClassName) {
            this.cacheClassName = cacheClassName;
            return getThis();
        }

        @Override
        public void copy(BusinessBuilder builder) {
            this.version = builder.version;
            this.type = builder.type;
            this.app = builder.app;
            this.tenant = builder.tenant;
            this.cacheName = builder.cacheName;
            this.suffix = builder.suffix;
            this.cacheClassName = builder.cacheClassName;
        }
    }

    public static class BusinessReuseBuilder extends BusinessCopyableBuilder<BusinessReuseBuilder>
    {

        protected String guid;

        public BusinessReuseBuilder(BusinessBuilder builder) {
            super(builder);
            type(CacheKeyConstants.TYPE_BUSINESS_REUSE);
        }

        public BusinessReuseBuilder guid(String guid) {
            this.guid = guid;
            return getThis();
        }

        @Override
        protected String buildString() {
            return join(CacheKeyConstants.KEY_SEPARATOR, version, type, app, tenant, cacheName, CacheKeyConstants.TYPE_BUSINESS, cacheClassName, CacheKeyConstants.TYPE_BUSINESS_REUSE, guid) + suffix;
        }

        @Override
        protected BusinessReuseBuilder getThis() {
            return this;
        }
    }

    public static class BusinessIndexBuilder extends BusinessCopyableBuilder<BusinessIndexBuilder>
    {
        protected String fieldName;
        protected String fieldValue;
        protected String guid;

        public BusinessIndexBuilder(BusinessBuilder builder) {
            super(builder);
            type(CacheKeyConstants.TYPE_BUSINESS_INDEX);
        }

        public BusinessIndexBuilder fieldName(String fieldName) {
            this.fieldName = fieldName;
            return this;
        }

        public BusinessIndexBuilder fieldValue(String fieldValue) {
            this.fieldValue = fieldValue;
            return this;
        }

        public BusinessIndexBuilder guid(String guid) {
            this.guid = guid;
            return this;
        }

        @Override
        protected String buildString() {
            return join(CacheKeyConstants.KEY_SEPARATOR, version, type, app, tenant, cacheName, CacheKeyConstants.TYPE_BUSINESS, cacheClassName, CacheKeyConstants.TYPE_BUSINESS_INDEX, join(CacheKeyConstants.KEY_SEGMENT_SEPARATOR, fieldName, fieldValue, guid)) + suffix;
        }

        @Override
        protected BusinessIndexBuilder getThis() {
            return this;
        }
    }

    public static class BusinessMethodBuilder extends BusinessCopyableBuilder<BusinessMethodBuilder>
    {

        protected String guid;
        protected String signature;

        public BusinessMethodBuilder(BusinessBuilder builder) {
            super(builder);
            type(CacheKeyConstants.TYPE_BUSINESS_METHOD);
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
        protected String buildString() {
            // TODO signture是不是要删掉
            return join(CacheKeyConstants.KEY_SEPARATOR, version, type, app, tenant, cacheName, CacheKeyConstants.TYPE_BUSINESS, CacheKeyConstants.TYPE_BUSINESS_METHOD, guid, signature) + suffix;
        }

        @Override
        protected BusinessMethodBuilder getThis() {
            return this;
        }
    }
}

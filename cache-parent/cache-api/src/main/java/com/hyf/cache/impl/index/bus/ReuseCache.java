// package com.hyf.cache.impl.index;
//
// import com.hyf.cache.Cache;
// import com.hyf.cache.enums.CacheType;
// import com.hyf.cache.impl.context.BaseCacheOperation;
// import com.hyf.cache.impl.context.InvocationContext;
// import com.hyf.cache.impl.el.EvaluationManager;
// import com.hyf.cache.impl.utils.TypeUtils;
// import org.springframework.context.expression.AnnotatedElementKey;
// import org.springframework.expression.EvaluationContext;
// import org.springframework.util.ClassUtils;
//
// import java.lang.reflect.Method;
// import java.lang.reflect.ParameterizedType;
// import java.lang.reflect.Type;
// import java.util.Collection;
// import java.util.Map;
// import java.util.concurrent.Callable;
// import java.util.concurrent.TimeUnit;
// import java.util.concurrent.locks.Lock;
//
// /**
//  * @author baB_hyf
//  * @date 2022/02/11
//  */
// public class ReuseCache implements Cache {
//
//     private Cache      delegate;
//     private IndexCache indexCache = new IndexCache();
//
//     public ReuseCache(Cache delegate) {
//         this.delegate = delegate;
//     }
//
//     public Class<?> getCacheClass(BaseCacheOperation operation, Object target, Method method, Object... params) {
//         Class<?> cacheClass = operation.getCacheClass();
//         if (cacheClass == null || cacheClass == Class.class) {
//             if (!"".equals(operation.getCacheClassName())) {
//                 try {
//                     cacheClass = ClassUtils.forName(operation.getCacheClassName(), null);
//                 }
//                 catch (ClassNotFoundException e) {
//                     throw new RuntimeException("Generate key failed", e);
//                 }
//             }
//             // 方法返回值查找默认
//             else {
//                 InvocationContext invocationContext = new InvocationContext(target, method, params);
//                 cacheClass = TypeUtils.getMethodActualReturnType(invocationContext.getMethod());
//             }
//         }
//
//         return cacheClass;
//     }
//
//
//     public Object getReuseObject(BaseCacheOperation operation, String[] mapperIdx, InvocationContext invocationContext) {
//
//         Class<?> cacheClass = getCacheClass(operation, invocationContext.getTarget(), invocationContext.getMethod(), invocationContext.getArgs());
//         // default index
//         String defaultIndexName = getIndexName(cacheClass);
//
//         String indexName = defaultIndexName;
//         String elKey = "";
//
//         // 默认唯一索引
//         if (mapperIdx.length == 0) {
//             elKey = indexName;
//         }
//         // 指定唯一索引
//         else if (mapperIdx.length == 1) {
//             String indexString = mapperIdx[0];
//             int aliasSplit = indexString.lastIndexOf(aliasSplitString);
//             if (aliasSplit != -1) {
//                 elKey = indexString.substring(0, aliasSplit);
//                 indexName = indexString.substring(aliasSplit + aliasSplitString.length());
//             }
//             else {
//                 elKey = indexString;
//                 indexName = indexString.replaceAll("#", "");
//             }
//         }
//         // 组合索引
//         else {
//             // TODO
//         }
//
//         for (String idx : mapperIdx) {
//             // EvaluationContext evaluationContext = EvaluationManager.createEvaluationContext(context.getInvocationContext());
//             // Object eval = EvaluationManager.eval(idx, new AnnotatedElementKey(), evaluationContext);
//
//         }
//
//         return null;
//     }
//
//
//
//
//     @Override
//     public void put(Object key, Object value, long ttl, TimeUnit unit) {
//
//     }
//
//     @Override
//     public void put(Object key, Object value, long ttl, TimeUnit unit, long randomMillis) {
//
//     }
//
//     @Override
//     public ValueWrapper putIfAbsent(Object key, Object value, long ttl, TimeUnit unit) {
//         return null;
//     }
//
//     @Override
//     public ValueWrapper putIfAbsent(Object key, Object value, long ttl, TimeUnit unit, long randomMillis) {
//         return null;
//     }
//
//     @Override
//     public void touch(Object key, long ttl, TimeUnit unit) {
//
//     }
//
//     @Override
//     public ValueWrapper getAll() {
//         return null;
//     }
//
//     @Override
//     public CacheType getCacheType() {
//         return null;
//     }
//
//     @Override
//     public Lock getLock(Object key) {
//         return null;
//     }
//
//     @Override
//     public String getName() {
//         return null;
//     }
//
//     @Override
//     public Object getNativeCache() {
//         return null;
//     }
//
//     @Override
//     public ValueWrapper get(Object key) {
//         return null;
//     }
//
//     @Override
//     public <T> T get(Object key, Class<T> type) {
//         return null;
//     }
//
//     @Override
//     public <T> T get(Object key, Callable<T> valueLoader) {
//         return null;
//     }
//
//     @Override
//     public void put(Object key, Object value) {
//
//     }
//
//     @Override
//     public void evict(Object key) {
//
//     }
//
//     @Override
//     public void clear() {
//
//     }
// }

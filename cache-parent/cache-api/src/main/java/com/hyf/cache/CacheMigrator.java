package com.hyf.cache;

/**
 * 考虑作为工具外部使用，提供一条迁移处理链来不断的升级迭代缓存，系统启动时处理
 *
 * @author baB_hyf
 * @date 2022/01/21
 */
public interface CacheMigrator
{

    int order();

    boolean supportVersion(String version);

    void migrate(Object key);
}

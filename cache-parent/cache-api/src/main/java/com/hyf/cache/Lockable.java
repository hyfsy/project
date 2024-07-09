package com.hyf.cache;

/**
 * 支持加锁，需要保证顺序一致
 *
 * @author baB_hyf
 * @date 2022/02/16
 */
public interface Lockable<T> extends Comparable<T>
{

    /**
     * 获取锁的名称
     * 
     * @return 锁的名称
     */
    String getLockName();
}

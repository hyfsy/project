package com.hyf.frame;

import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * @author baB_hyf
 * @date 2022/02/16
 */
public class T6 {

    public static void main(String[] args) {
        ReentrantReadWriteLock reentrantReadWriteLock = new ReentrantReadWriteLock();
        ReentrantReadWriteLock.ReadLock readLock = reentrantReadWriteLock.readLock();
        ReentrantReadWriteLock.WriteLock writeLock = reentrantReadWriteLock.writeLock();
        readLock.lock();
        writeLock.lock();
        System.out.println();
    }
}

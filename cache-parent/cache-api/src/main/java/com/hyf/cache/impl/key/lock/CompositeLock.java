package com.hyf.cache.impl.key.lock;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

/**
 * 全局锁列表生成的组合锁
 * 
 * @author baB_hyf
 * @date 2022/02/16
 */
public class CompositeLock implements Lock
{

    private static final int SUCCESS = -1;

    private final List<Lock> locks;

    public CompositeLock(List<Lock> locks) {
        this.locks = locks;
    }

    @Override
    public void lock() {
        locks.forEach(Lock::lock);
    }

    @Override
    public void lockInterruptibly() throws InterruptedException {
        int failedIdx = SUCCESS;
        try {
            for (int i = 0; i < locks.size(); i++) {
                try {
                    locks.get(i).lockInterruptibly();
                }
                catch (InterruptedException e) {
                    failedIdx = i;
                    throw e;
                }
            }
        }
        finally {
            unlockUntilIfNecessary(failedIdx);
        }
    }

    @Override
    public boolean tryLock() {
        int failedIdx = SUCCESS;

        try {
            for (int i = 0; i < locks.size(); i++) {
                if (!locks.get(i).tryLock()) {
                    failedIdx = i;
                    break;
                }
            }
        }
        finally {
            unlockUntilIfNecessary(failedIdx);
        }

        return failedIdx == SUCCESS;
    }

    @Override
    public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
        int failedIdx = SUCCESS;
        try {
            for (int i = 0; i < locks.size(); i++) {
                try {
                    if (!locks.get(i).tryLock(time, unit)) {
                        failedIdx = i;
                        break;
                    }
                }
                catch (InterruptedException e) {
                    failedIdx = i;
                    throw e;
                }
            }
        }
        finally {
            unlockUntilIfNecessary(failedIdx);
        }

        return failedIdx == SUCCESS;
    }

    @Override
    public void unlock() {
        locks.forEach(Lock::unlock);
    }

    @Override
    public Condition newCondition() {
        throw new UnsupportedOperationException("newCondition");
    }

    /**
     * 从前往后顺序解锁
     * 
     * @param unitIdx
     *            加锁失败的索引位置
     */
    private void unlockUntilIfNecessary(int unitIdx) {
        if (unitIdx <= 0) {
            return;
        }

        for (int i = 0; i < locks.size(); i++) {
            if (i == unitIdx) {
                break;
            }
            locks.get(i).unlock();
        }
    }
}

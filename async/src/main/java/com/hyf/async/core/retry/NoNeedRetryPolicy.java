package com.hyf.async.core.retry;

import com.hyf.async.entity.AsyncLog;

/**
 * @author Administrator
 */
public class NoNeedRetryPolicy implements RetryPolicy {

    @Override
    public boolean needRetry(AsyncLog asyncLog) {
        return false;
    }
}

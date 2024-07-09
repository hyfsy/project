package com.hyf.async.core.retry;

import com.hyf.async.entity.AsyncLog;

/**
 * @author Administrator
 */
public interface RetryPolicy {

    boolean needRetry(AsyncLog asyncLog);
}

package com.hyf.async.core.retry;

import com.hyf.async.entity.AsyncLog;
import org.springframework.util.Assert;

/**
 * @author Administrator
 */
public class CountBasedRetryPolicy implements RetryPolicy {

    private final int retryCount;

    public CountBasedRetryPolicy(int retryCount) {
        Assert.state(retryCount >= 0, "retryCount must be a positive number");
        this.retryCount = retryCount;
    }

    @Override
    public boolean needRetry(AsyncLog asyncLog) {
        return asyncLog.getRetryCount() < retryCount;
    }
}

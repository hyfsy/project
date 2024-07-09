package com.hyf.async.core.retry;

import com.hyf.async.constant.AsyncLogConstants;
import com.hyf.async.entity.AsyncLog;

/**
 * @author Administrator
 */
public class DefaultRetryPolicy implements RetryPolicy {

    @Override
    public boolean needRetry(AsyncLog asyncLog) {
        return asyncLog.getRetryCount() < AsyncLogConstants.MAX_RETRY_COUNT;
    }
}

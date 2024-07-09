package com.hyf.async.core;

import com.hyf.async.core.retry.RetryPolicy;
import com.hyf.async.entity.AsyncLog;

import java.util.concurrent.ExecutorService;

/**
 * @author Administrator
 */
public interface AsyncProcessor {

    String getType();

    void runTask(AsyncLog asyncLog) throws Throwable;

    // TODO 迁移到config？
    RetryPolicy getRetryPolicy();

    void handleStart(AsyncTask task);

    void handleRetryStart(AsyncTask task);

    void handleException(AsyncTask task, Throwable t);

    void handleSucceed(AsyncTask task);

    void handleCompletion(AsyncTask task);

    void handleDead(AsyncTask task, Throwable t);

    ExecutorService getPool(AsyncTask task);
}

package com.hyf.async.core;

import com.hyf.async.core.retry.DefaultRetryPolicy;
import com.hyf.async.core.retry.RetryPolicy;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ExecutorService;

/**
 * @author Administrator
 */
@Slf4j
public abstract class AsyncProcessorAdapter implements AsyncProcessor {

    @Override
    public RetryPolicy getRetryPolicy() {
        return new DefaultRetryPolicy();
    }

    @Override
    public void handleStart(AsyncTask task) {
        log.info("Async task start: " + task.getId());
    }

    @Override
    public void handleRetryStart(AsyncTask task) {
        log.info("Async task retry start: " + task.getId());
    }

    @Override
    public void handleException(AsyncTask task, Throwable t) {
        log.error("Async task failed: " + task.getId(), t);
    }

    @Override
    public void handleSucceed(AsyncTask task) {
        log.info("Async task succeed: " + task.getId());
    }

    @Override
    public void handleCompletion(AsyncTask task) {
        log.info("Async task completion: " + task.getId());
    }

    @Override
    public void handleDead(AsyncTask task, Throwable t) {
        log.error("Async task dead: " + task.getId());
    }

    @Override
    public ExecutorService getPool(AsyncTask task) {
        return null;
    }
}

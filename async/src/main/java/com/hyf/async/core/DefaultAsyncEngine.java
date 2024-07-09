package com.hyf.async.core;

import com.hyf.async.constant.AsyncLogConstants;
import com.hyf.async.core.retry.RetryPolicy;
import com.hyf.async.entity.AsyncLog;
import com.hyf.async.service.IAsyncLogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author Administrator
 */
@Slf4j
@Component
public class DefaultAsyncEngine implements AsyncEngine {

    @Autowired
    private IAsyncLogService asyncLogService;

    @Autowired(required = false)
    private List<AsyncProcessor> processors = new ArrayList<>();

    private Map<String, AsyncProcessor> processorMap = new HashMap<>();

    // TODO config
    private ExecutorService ASYNC_EXECUTOR =
        new ThreadPoolExecutor(10, 10, 30, TimeUnit.SECONDS, new LinkedBlockingQueue<>(1000), new ThreadFactory() {
            private final AtomicInteger i = new AtomicInteger(0);

            @Override
            public Thread newThread(Runnable r) {
                Thread t = new Thread(r);
                t.setName("async-log-process-" + i.getAndIncrement());
                return t;
            }
        });

    @PostConstruct
    public void post() {
        processorMap =
            processors.stream().collect(Collectors.toMap(AsyncProcessor::getType, Function.identity(), (o1, o2) -> {
                throw new AsyncException("AsyncProcessor's type must not be same: " + o1.getType());
            }));
    }

    @Override
    public void submit(AsyncTask task) {

        AsyncProcessor processor = processorMap.get(task.getAsyncLog().getType());

        if (processor == null) {
            AsyncLog asyncLog = task.getAsyncLog();
            asyncLog.setRetryCount(AsyncLogConstants.MAX_RETRY_COUNT);
            asyncLog.setFailCause("No processor found");
            asyncLog.setStatus(AsyncLogConstants.STATUS_FAILED);
            // TODO log不存在库内的情况
            asyncLogService.updateLog(asyncLog);
            return;
        }

        ExecutorService executorService = processor.getPool(task);
        try {
            if (executorService != null) {
                executorService.submit(new AsyncTaskWrapper(() -> runProcessor(processor, task)));
            } else {
                ASYNC_EXECUTOR.submit(new AsyncTaskWrapper(() -> runProcessor(processor, task)));
            }
        } catch (Exception e) {
            log.error("Run async task failed, taskId: " + task.getId(), e);
        }
    }

    private void runProcessor(AsyncProcessor processor, AsyncTask task) {

        AsyncLog asyncLog = task.getAsyncLog();

        if (asyncLog.getStatus() == AsyncLogConstants.STATUS_RUNNING
            || asyncLog.getStatus() == AsyncLogConstants.STATUS_RETRYING) {
            log.warn("Ignore current running task: " + task.getId());
            return;
        }

        RetryPolicy retryPolicy = processor.getRetryPolicy();
        if ((asyncLog.getStatus() == AsyncLogConstants.STATUS_RETRYING
            || asyncLog.getStatus() == AsyncLogConstants.STATUS_FAILED) && !retryPolicy.needRetry(asyncLog)) {
            log.warn("Ignore not need retry task: " + task.getId());
            return;
        }

        try {
            if (asyncLog.getStatus() == AsyncLogConstants.STATUS_NOT_START) {
                asyncLog.setStatus(AsyncLogConstants.STATUS_RUNNING);
            } else {
                asyncLog.setStatus(AsyncLogConstants.STATUS_RETRYING);
            }

            if (!asyncLogService.updateLog(asyncLog)) {
                log.warn("Task has been running: " + task.getId());
                return;
            }
            if (asyncLog.getStatus() == AsyncLogConstants.STATUS_RUNNING) {
                processor.handleStart(task);
            } else if (asyncLog.getStatus() == AsyncLogConstants.STATUS_RETRYING) {
                processor.handleRetryStart(task);
            }
            processor.runTask(asyncLog);
            processor.handleSucceed(task);
            asyncLog.setStatus(AsyncLogConstants.STATUS_SUCCEED);
            asyncLogService.updateLog(asyncLog);
        } catch (Throwable e) {
            try {
                processor.handleException(task, e);
            } catch (Throwable ex) {
                log.error("Failed to run async task: " + task.getId(), ex);
                // update failed status
                asyncLog.setRetryCount(asyncLog.getRetryCount() + 1);
                asyncLog.setFailCause(e.getMessage());
                asyncLog.setStatus(AsyncLogConstants.STATUS_FAILED);
                asyncLogService.updateLog(asyncLog);
            }

            if (!retryPolicy.needRetry(asyncLog)) {
                processor.handleDead(task, e);
            }
        } finally {
            processor.handleCompletion(task);
        }
    }

    @Override
    public void stop(AsyncTask task) {
        throw new UnsupportedOperationException();
    }
}

package com.hyf.async.core;

import com.hyf.async.entity.AsyncLog;
import com.hyf.async.service.IAsyncLogService;
import com.hyf.async.utils.ApplicationUtils;
import org.springframework.util.Assert;

import java.util.UUID;

/**
 * @author Administrator
 */
public class AsyncTask {

    private String id;
    private AsyncLog asyncLog;
    private AsyncConfig config;

    public AsyncTask(Long logId) {
        this(logId, createDefaultConfig());
    }

    public AsyncTask(Long logId, AsyncConfig config) {
        this.id = UUID.randomUUID().toString();
        this.asyncLog = findLog(logId);
        this.config = config;
        check();
    }

    public AsyncTask(AsyncLog asyncLog) {
        this(asyncLog, createDefaultConfig());
    }

    public AsyncTask(AsyncLog asyncLog, AsyncConfig config) {
        this.id = UUID.randomUUID().toString();
        this.asyncLog = asyncLog;
        this.config = config;
        check();
    }

    private AsyncLog findLog(Long logId) {
        IAsyncLogService asyncLogService = ApplicationUtils.getBean(IAsyncLogService.class);
        AsyncLog asyncLog = asyncLogService.getById(logId);
        if (asyncLog == null) {
            throw new IllegalArgumentException("logId cannot find task: " + logId);
        }
        return asyncLog;
    }

    private void check() {
        Assert.notNull(asyncLog, "AsyncLog is null");
        Assert.notNull(config, "AsyncConfig is null");
    }

    public static AsyncConfig createDefaultConfig() {
        return new AsyncConfig();
    }

    public String getId() {
        return id;
    }

    public AsyncLog getAsyncLog() {
        return asyncLog;
    }

    public AsyncConfig getConfig() {
        return config;
    }
}

package com.hyf.async.config;

import com.hyf.async.constant.AsyncLogConstants;
import com.hyf.async.core.AsyncEngine;
import com.hyf.async.core.AsyncTask;
import com.hyf.async.entity.AsyncLog;
import com.hyf.async.service.IAsyncLogService;
import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author Administrator
 */
@Slf4j
@Configuration
@ComponentScan("com.hyf.async")
@MapperScan("com.hyf.async.mapper")
public class AsyncLogConfiguration {

    @Autowired
    private AsyncEngine engine;
    @Autowired
    private IAsyncLogService asyncLogService;

    // TODO config
    @Scheduled(cron = "0 0/1 * * * ?")
    public void runTask() {
        List<AsyncLog> asyncLogs = asyncLogService.list();

        for (AsyncLog asyncLog : asyncLogs) {
            AsyncTask asyncTask = new AsyncTask(asyncLog);
            engine.submit(asyncTask);
        }

    }

    // TODO remove
    @Scheduled(cron = "0 0 0 1/1 * ? ")
    public void removeOutOfTime() {
        List<AsyncLog> asyncLogs = asyncLogService.list();

        LocalDateTime yesterday = LocalDateTime.now().minusDays(1);
        for (AsyncLog asyncLog : asyncLogs) {
            // TODO config
            if (asyncLog.getUpdateTime().isBefore(yesterday)) {
                if (asyncLog.getStatus() != AsyncLogConstants.STATUS_SUCCEED) {
                    log.error("Task running for a long time, automatically cleaned, log info: " + asyncLog);
                }
                asyncLogService.removeById(asyncLog);
            }

        }
    }

    // TODO update time check

}

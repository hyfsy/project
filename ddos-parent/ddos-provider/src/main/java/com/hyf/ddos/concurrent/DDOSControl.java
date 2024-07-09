package com.hyf.ddos.concurrent;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author baB_hyf
 * @date 2021/09/21
 */
public class DDOSControl {

    public static volatile Integer         RUNNABLE_COUNT   = null;
    public static volatile Integer         THREAD_SIZE      = null;
    public static volatile ExecutorService EXECUTOR_SERVICE = null;

    public static void invoke(Runnable runnable) {
        AtomicInteger failedCount = new AtomicInteger(DDOSRunnable.FAILED_COUNT);

        for (int i = 0; i < RUNNABLE_COUNT; i++) {
            EXECUTOR_SERVICE.submit(new DDOSRunnable(runnable, failedCount));
        }
    }

    public static void stop() {
        if (DDOSControl.EXECUTOR_SERVICE == null) {
            return;
        }

        DDOSControl.EXECUTOR_SERVICE.shutdownNow();
        DDOSControl.EXECUTOR_SERVICE = DDOSExecutorServiceFactory.createExecutorService();
    }
}

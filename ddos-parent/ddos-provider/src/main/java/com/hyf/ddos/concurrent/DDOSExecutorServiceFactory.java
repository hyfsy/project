package com.hyf.ddos.concurrent;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author baB_hyf
 * @date 2021/09/22
 */
public class DDOSExecutorServiceFactory {

    public static ExecutorService createExecutorService() {
        return new ThreadPoolExecutor(DDOSControl.THREAD_SIZE,
                DDOSControl.THREAD_SIZE,
                0L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<>(),
                new ThreadFactory() {
                    private final AtomicInteger i = new AtomicInteger(0);

                    @Override
                    public Thread newThread(Runnable r) {
                        return new Thread(r, "ddos-thread-" + i.getAndIncrement());
                    }
                },
                new ThreadPoolExecutor.AbortPolicy());
    }
}

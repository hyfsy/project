package com.hyf.cache.custom.caffeine;

import java.util.concurrent.*;

import javax.annotation.PreDestroy;

import org.checkerframework.checker.index.qual.Positive;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.springframework.stereotype.Component;

import com.github.benmanes.caffeine.cache.Scheduler;

/**
 * @author baB_hyf
 * @date 2022/02/17
 */
@Component("CaffeineScheduler")
public class CaffeineScheduler implements Scheduler
{

    private final ExecutorService executorService = Executors.newSingleThreadExecutor();

    @PreDestroy
    public void pre() {
        executorService.shutdown();
    }

    @Override
    public @NonNull Future<?> schedule(@NonNull Executor executor, @NonNull Runnable command, @Positive long delay,
            @NonNull TimeUnit unit) {
        System.out.println("CaffeineScheduler");
        return executorService.submit(command);
    }
}

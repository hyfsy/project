package com.hyf.cache.custom.caffeine;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.springframework.stereotype.Component;

/**
 * @author baB_hyf
 * @date 2022/02/17
 */
@Component("CaffeineExecutor")
public class CaffeineExecutor implements Executor
{

    private final ExecutorService executorService = Executors.newSingleThreadExecutor();

    @Override
    public void execute(Runnable command) {
        System.out.println("CaffeineExecutor");
        executorService.execute(command);
    }
}

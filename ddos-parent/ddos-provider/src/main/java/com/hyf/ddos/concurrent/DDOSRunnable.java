package com.hyf.ddos.concurrent;

import com.hyf.ddos.log.Log;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author baB_hyf
 * @date 2021/09/21
 */
public class DDOSRunnable implements Runnable {

    public static volatile Integer FAILED_COUNT = null;
    public static volatile Boolean STOP         = null;

    private final Runnable      runnable;
    private final AtomicInteger failedCount;
    private final boolean       maybeNeedFailStop;

    public DDOSRunnable(Runnable runnable, AtomicInteger failedCount) {
        this.runnable = runnable;
        this.failedCount = failedCount;
        this.maybeNeedFailStop = failedCount.get() < 0;
    }

    @Override
    public void run() {

        try {

            while (true && !STOP) {
                try {
                    runnable.run();
                } catch (Throwable e) {
                    Log.debug("failed run instruction: {}", e.getMessage());

                    if (maybeNeedFailStop) {
                        int i = failedCount.decrementAndGet();
                        if (i <= 0) {
                            throw new InterruptedException();
                        }
                    }
                }
            }

            Log.debug("Runnable finished.");
        } catch (Exception e) {
            if (!(e instanceof InterruptedException)) {
                Log.error("Failed to execute runnable ", e);
            }
        }
    }
}

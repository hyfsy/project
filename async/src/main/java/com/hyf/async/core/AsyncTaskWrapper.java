package com.hyf.async.core;

import org.apache.skywalking.apm.toolkit.trace.TraceCrossThread;

/**
 * @author Administrator
 */
@TraceCrossThread
public class AsyncTaskWrapper implements Runnable {

    private Runnable r;

    public AsyncTaskWrapper(Runnable r) {
        this.r = r;
    }

    @Override
    public void run() {
        r.run();
    }
}

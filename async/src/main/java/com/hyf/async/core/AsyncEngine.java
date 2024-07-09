package com.hyf.async.core;

/**
 * @author Administrator
 */
public interface AsyncEngine {

    void submit(AsyncTask task);

    void stop(AsyncTask task);

}

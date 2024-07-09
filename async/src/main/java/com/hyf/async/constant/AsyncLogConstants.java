package com.hyf.async.constant;

/**
 * @author Administrator
 */
public class AsyncLogConstants {

    public static final String API_ASYNC_LOG = "/product/async/log";

    public static final int STATUS_NOT_START = 10;
    public static final int STATUS_RUNNING = 20;
    public static final int STATUS_RETRYING = 25;
    public static final int STATUS_SUCCEED = 30;
    public static final int STATUS_FAILED = 50;

    public static final int MAX_RETRY_COUNT = 5;

}

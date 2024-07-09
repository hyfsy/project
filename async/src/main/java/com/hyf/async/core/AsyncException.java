package com.hyf.async.core;

/**
 * @author Administrator
 */
public class AsyncException extends RuntimeException {

    public AsyncException() {
        super();
    }

    public AsyncException(String message) {
        super(message);
    }

    public AsyncException(String message, Throwable cause) {
        super(message, cause);
    }

}

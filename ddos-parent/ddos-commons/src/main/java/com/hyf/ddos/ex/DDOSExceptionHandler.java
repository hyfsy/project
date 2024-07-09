package com.hyf.ddos.ex;

import com.hyf.ddos.log.Log;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @author baB_hyf
 * @date 2021/09/21
 */
@RestControllerAdvice(basePackages = "com.hyf.ddos.controller")
public class DDOSExceptionHandler {

    @ExceptionHandler
    public void throwableHandler(Throwable t) {
        Log.error("failed to invoke endpoint", t);
    }
}

package com.hyf.ddos.service;

import org.springframework.beans.BeansException;
import org.springframework.cloud.openfeign.FeignClientBuilder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * @author baB_hyf
 * @date 2021/09/21
 */
@Component
public class InvokeBuilder implements ApplicationContextAware {

    private static ApplicationContext applicationContext;

    public static <T> T build(Class<T> clazz, String url) {
        return new FeignClientBuilder(applicationContext).forType(clazz, url).url(url).build();
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        InvokeBuilder.applicationContext = applicationContext;
    }
}

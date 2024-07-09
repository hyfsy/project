package com.hyf.cache.custom.caffeine;

import org.springframework.stereotype.Component;

import com.github.benmanes.caffeine.cache.Ticker;

/**
 * @author baB_hyf
 * @date 2022/02/17
 */
@Component("CaffeineTicker")
public class CaffeineTicker implements Ticker
{

    @Override
    public long read() {
        System.out.println("CaffeineTicker");
        return 10000;
    }
}

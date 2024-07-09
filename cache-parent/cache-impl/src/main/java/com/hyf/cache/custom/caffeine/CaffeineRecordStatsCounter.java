package com.hyf.cache.custom.caffeine;

import java.util.function.Supplier;

import org.springframework.stereotype.Component;

import com.github.benmanes.caffeine.cache.stats.StatsCounter;

/**
 * @author baB_hyf
 * @date 2022/02/17
 */
@Component("CaffeineRecordStatsCounter")
public class CaffeineRecordStatsCounter implements Supplier<StatsCounter>
{

    @Override
    public StatsCounter get() {
        System.out.println("CaffeineRecordStatsCounter");
        return StatsCounter.disabledStatsCounter();
    }
}

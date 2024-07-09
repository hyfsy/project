package com.hyf.cache.impl.properties;

import java.time.Duration;

/**
 * @author baB_hyf
 * @date 2022/03/05
 */
public final class Caffeine {

    private boolean enabled = true;

    private int      initialCapacity = -1;
    private Duration expireAfterAccess;
    private Duration refreshAfterWrite;
    private String   removalListener;
    private String   evictionListener;
    private long     maximumSize     = -1;
    private long     maximumWeight   = -1;
    private boolean  recordStats;
    private String   recordStatsCounter;
    private boolean  weakKeys;
    private boolean  weakValues;
    private boolean  softValues;
    private String   weigher;
    private String   scheduler;
    private String   ticker;
    private String   executor;
    // not support
    private boolean  async;

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public int getInitialCapacity() {
        return initialCapacity;
    }

    public void setInitialCapacity(int initialCapacity) {
        this.initialCapacity = initialCapacity;
    }

    public Duration getExpireAfterAccess() {
        return expireAfterAccess;
    }

    public void setExpireAfterAccess(Duration expireAfterAccess) {
        this.expireAfterAccess = expireAfterAccess;
    }

    public Duration getRefreshAfterWrite() {
        return refreshAfterWrite;
    }

    public void setRefreshAfterWrite(Duration refreshAfterWrite) {
        this.refreshAfterWrite = refreshAfterWrite;
    }

    public String getRemovalListener() {
        return removalListener;
    }

    public void setRemovalListener(String removalListener) {
        this.removalListener = removalListener;
    }

    public String getEvictionListener() {
        return evictionListener;
    }

    public void setEvictionListener(String evictionListener) {
        this.evictionListener = evictionListener;
    }

    public long getMaximumSize() {
        return maximumSize;
    }

    public void setMaximumSize(long maximumSize) {
        this.maximumSize = maximumSize;
    }

    public long getMaximumWeight() {
        return maximumWeight;
    }

    public void setMaximumWeight(long maximumWeight) {
        this.maximumWeight = maximumWeight;
    }

    public boolean isRecordStats() {
        return recordStats;
    }

    public void setRecordStats(boolean recordStats) {
        this.recordStats = recordStats;
    }

    public String getRecordStatsCounter() {
        return recordStatsCounter;
    }

    public void setRecordStatsCounter(String recordStatsCounter) {
        this.recordStatsCounter = recordStatsCounter;
    }

    public boolean isWeakKeys() {
        return weakKeys;
    }

    public void setWeakKeys(boolean weakKeys) {
        this.weakKeys = weakKeys;
    }

    public boolean isWeakValues() {
        return weakValues;
    }

    public void setWeakValues(boolean weakValues) {
        this.weakValues = weakValues;
    }

    public boolean isSoftValues() {
        return softValues;
    }

    public void setSoftValues(boolean softValues) {
        this.softValues = softValues;
    }

    public String getWeigher() {
        return weigher;
    }

    public void setWeigher(String weigher) {
        this.weigher = weigher;
    }

    public String getScheduler() {
        return scheduler;
    }

    public void setScheduler(String scheduler) {
        this.scheduler = scheduler;
    }

    public String getTicker() {
        return ticker;
    }

    public void setTicker(String ticker) {
        this.ticker = ticker;
    }

    public String getExecutor() {
        return executor;
    }

    public void setExecutor(String executor) {
        this.executor = executor;
    }

    public boolean isAsync() {
        return async;
    }

    public void setAsync(boolean async) {
        this.async = async;
    }
}

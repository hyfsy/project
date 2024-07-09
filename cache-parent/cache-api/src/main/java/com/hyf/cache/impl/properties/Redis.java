package com.hyf.cache.impl.properties;

/**
 * @author baB_hyf
 * @date 2022/03/05
 */
public final class Redis {

    private boolean enabled = true;

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}

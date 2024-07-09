package com.hyf.cache.impl.properties;

import java.util.ArrayList;
import java.util.List;

/**
 * @author baB_hyf
 * @date 2022/03/05
 */
public class ReuseDegrade {

    private long defaultThreshold = 100_000L;

    private List<Rule> rules = new ArrayList<>();

    public long getDefaultThreshold() {
        return defaultThreshold;
    }

    public List<Rule> getRules() {
        return rules;
    }

    public static class Rule {

        private Class<?> name;

        // -1 表示不进行降级处理
        private long threshold;

        public Class<?> getName() {
            return name;
        }

        public void setName(Class<?> name) {
            this.name = name;
        }

        public long getThreshold() {
            return threshold;
        }

        public void setThreshold(long threshold) {
            this.threshold = threshold;
        }
    }
}

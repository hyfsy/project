
package com.hyf.proxyserver.server.capturer;

import com.hyf.proxyserver.server.relay.RelayContext;

public interface TrafficCapturer {

    /**
     * 过滤包
     *
     * @param context 中继上下文
     * @return 过滤包返回true，否则返回false
     */
    default boolean accept(RelayContext<?> context) {
        return true;
    }

    /**
     * 流量捕获
     *
     * @param context 中继上下文
     */
    void capture(RelayContext<?> context);

}

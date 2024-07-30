package com.hyf.proxyserver.extensions;

import com.hyf.proxyserver.server.capturer.TrafficCapturer;
import io.netty.channel.Channel;

public class DefaultTrafficCapturer implements TrafficCapturer {
    @Override
    public Object capture(Channel inboundChannel, Channel outboundChannel, Object msg, boolean fromClient) {
        // 修改数据
        return msg;
    }
}

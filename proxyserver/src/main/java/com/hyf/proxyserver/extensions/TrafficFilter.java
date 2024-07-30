package com.hyf.proxyserver.extensions;

import io.netty.channel.Channel;

public interface TrafficFilter {
    boolean filter(Channel inboundChannel, Channel outboundChannel, Object msg, boolean fromClient);
}

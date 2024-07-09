package com.hyf.proxyserver.extensions;

import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;

public interface TrafficFilter {
    boolean filter(Channel inboundChannel, Channel outboundChannel, ByteBuf msg, boolean fromClient);
}

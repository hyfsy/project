package com.hyf.proxyserver.server.relay;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;

public class DefaultRelayContext extends RelayContext<Object> {
    public DefaultRelayContext(ChannelHandlerContext ctx, Channel outboundChannel, Object msg) {
        super(ctx, outboundChannel, msg);
    }
}

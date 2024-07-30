package com.hyf.proxyserver.extensions;

import com.hyf.proxyserver.server.relay.RelayChannelInitializer;
import io.netty.channel.Channel;

public class DefaultRelayChannelInitializer implements RelayChannelInitializer {
    @Override
    public void initChannelBeforeRelay(Channel inboundChannel, Channel outboundChannel) {
        // 对特定协议的编解码支持，如 websocket/h2/quic
    }
}

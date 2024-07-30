package com.hyf.proxyserver.server.relay;

import io.netty.channel.Channel;

public interface RelayChannelInitializer {

    /**
     * 初始化通道
     *
     * @param inboundChannel  客户端到代理服务器的通道
     * @param outboundChannel 代理服务器到目标端的通道
     */
    default void initChannelBeforeRelay(Channel inboundChannel, Channel outboundChannel) {
    }

    /**
     * 初始化通道
     *
     * @param inboundChannel  客户端到代理服务器的通道
     * @param outboundChannel 代理服务器到目标端的通道
     */
    default void initChannelAfterRelay(Channel inboundChannel, Channel outboundChannel) {
    }
}

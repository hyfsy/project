package com.hyf.proxyserver.server;

import io.netty.channel.Channel;

public interface RelayChannelInitializer {

    /**
     * 初始化通道
     *
     * @param inboundChannel  客户端到代理服务器的通道
     * @param outboundChannel 代理服务器到目标端的通道
     */
    void initChannel(Channel inboundChannel, Channel outboundChannel);
}

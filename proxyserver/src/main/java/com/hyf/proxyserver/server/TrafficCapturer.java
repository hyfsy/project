
package com.hyf.proxyserver.server;

import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;

public interface TrafficCapturer {

    /**
     * 过滤包
     *
     * @param inboundChannel  入站通道
     * @param outboundChannel 出站通道
     * @param msg             监听的消息
     * @param fromClient      消息是客户端发的返回true，否则返回false
     * @return 过滤包返回true，否则返回false
     */
    default boolean filter(Channel inboundChannel, Channel outboundChannel, ByteBuf msg, boolean fromClient) {
        return true;
    }

    /**
     * 流量捕获
     *
     * @param inboundChannel  入站通道
     * @param outboundChannel 出站通道
     * @param msg             监听的消息
     * @param fromClient      消息是客户端发的返回true，否则返回false
     */
    ByteBuf capture(Channel inboundChannel, Channel outboundChannel, ByteBuf msg, boolean fromClient);

}

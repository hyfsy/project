package com.hyf.proxyserver.server.capturer;

import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;

public abstract class ByteBufTrafficCapturer extends SimpleTrafficCapturer<ByteBuf> {

    @Override
    protected ByteBuf capture0(Channel inboundChannel, Channel outboundChannel, ByteBuf msg, boolean fromClient) {
        return captureByteBuf(inboundChannel, outboundChannel, msg, fromClient);
    }

    protected abstract ByteBuf captureByteBuf(Channel inboundChannel, Channel outboundChannel, ByteBuf msg, boolean fromClient);
}

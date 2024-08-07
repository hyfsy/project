package com.hyf.proxyserver.server.capturer;

import com.hyf.proxyserver.server.relay.RelayContext;
import io.netty.buffer.ByteBuf;

public abstract class ByteBufTrafficListener extends SimpleTrafficListener<ByteBuf> {

    @Override
    protected void capture0(RelayContext<ByteBuf> context) {
        captureByteBuf(context);
    }

    protected abstract void captureByteBuf(RelayContext<ByteBuf> context);
}

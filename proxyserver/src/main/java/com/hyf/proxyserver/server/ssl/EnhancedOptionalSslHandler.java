package com.hyf.proxyserver.server.ssl;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslHandler;

import java.util.List;

/**
 * 支持ssl的情况对管道进行任意操作，灵活度更高，同时提供ssl启禁用的事件功能
 */
public abstract class EnhancedOptionalSslHandler extends ByteToMessageDecoder {

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        if (in.readableBytes() < 5) { // SslUtils.SSL_RECORD_HEADER_LENGTH
            return;
        }
        if (SslHandler.isEncrypted(in)) {
            handleSsl(ctx);
            ctx.fireUserEventTriggered(SslEnabledEvent.INSTANCE);
        } else {
            handleNonSsl(ctx);
            ctx.fireUserEventTriggered(SslNotEnabledEvent.INSTANCE);
        }
    }

    protected void handleSsl(ChannelHandlerContext ctx) {
        ctx.pipeline().remove(this);
    }

    protected void handleNonSsl(ChannelHandlerContext ctx) {
        ctx.pipeline().remove(this);
    }

    protected SslHandler newSslHandlerServer(ChannelHandlerContext ctx, SslContext sslContext) {
        return sslContext.newHandler(ctx.alloc());
    }

    protected SslHandler newSslHandlerClient(ChannelHandlerContext ctx, SslContext sslContext, String peerHost, int peerPort) {
        return sslContext.newHandler(ctx.alloc(), peerHost, peerPort);
    }

    protected String newSslHandlerName() {
        return SslUtils.getSslHandlerName();
    }
}

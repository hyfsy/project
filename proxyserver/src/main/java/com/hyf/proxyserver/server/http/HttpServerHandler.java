package com.hyf.proxyserver.server.http;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.*;

/**
 * 这里进来只可能是非socks的http代理，所以无需考虑socks，进行正常http代理认证流程即可
 */
@ChannelHandler.Sharable
public class HttpServerHandler extends ChannelInboundHandlerAdapter {

    public static final HttpServerHandler INSTANCE = new HttpServerHandler();

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

        if (msg instanceof HttpRequest) {
            ctx.pipeline().addAfter(ctx.name(), HttpServerConnectHandler.class.getName(), new HttpServerConnectHandler());
            ctx.pipeline().remove(this);
        }

        super.channelRead(ctx, msg);
    }
}

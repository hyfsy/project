package com.hyf.proxyserver.server.socks;

import com.hyf.proxyserver.server.ssl.SslUtils;
import com.hyf.proxyserver.server.ssl.SslEnabledEvent;
import com.hyf.proxyserver.server.ssl.SslNotEnabledEvent;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.net.InetSocketAddress;

/**
 * 在本地端决定好了是否使用ssl协议后，再对目标端进行管道配置，目标端需通过 {@link SslAwarePaddingChannelHandler} 配合使用
 * <p>
 * 场景：socks连接建立完毕，因为socks包装了ssl或其他报文，建立连接时无法判断该socks内部的报文是否需要使用ssl，导致建立连接完毕后，
 * 无法对客户端进行ssl的管道配置，所以目标端客户端侧使用这个来在服务端确定要添加ssl功能后同步配置目标端客户端的ssl功能
 */
public class SslAwareChannelHandler extends ChannelInboundHandlerAdapter {

    private final Channel channel;
    private final InetSocketAddress socketAddress;
    private ChannelHandler paddingChannelHandler;

    public SslAwareChannelHandler(Channel channel, InetSocketAddress socketAddress) {
        this.channel = channel;
        this.socketAddress = socketAddress;
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (paddingChannelHandler != null) {
            if (evt instanceof SslEnabledEvent) {
                channel.pipeline().replace(paddingChannelHandler, SslUtils.getSslHandlerName(), SslUtils.getClientSslContext().newHandler(ctx.alloc(), socketAddress.getHostString(), socketAddress.getPort()));
            } else if (evt instanceof SslNotEnabledEvent) {
                channel.pipeline().remove(paddingChannelHandler);
            }
        }
        super.userEventTriggered(ctx, evt);
    }

    public SslAwareChannelHandler prepareAssociation() {
        this.paddingChannelHandler = new SslAwarePaddingChannelHandler();
        this.channel.pipeline().addLast(this.paddingChannelHandler);
        return this;
    }

    /**
     * 仅用来替换
     */
    private static class SslAwarePaddingChannelHandler extends ChannelInboundHandlerAdapter {
    }
}

package com.hyf.proxyserver;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.proxy.HttpProxyHandler;
import io.netty.handler.proxy.Socks5ProxyHandler;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;

import java.io.File;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.nio.charset.StandardCharsets;

public class MitmClient {

    private final MitmClientConfig mitmClientConfig;
    private Channel channel;

    public MitmClient(MitmClientConfig mitmClientConfig) {
        this.mitmClientConfig = mitmClientConfig;
    }

    public void start(ResponseHandler responseHandler) {
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        Bootstrap b = new Bootstrap();

        InetSocketAddress proxyAddr = new InetSocketAddress(mitmClientConfig.getProxyHost(), mitmClientConfig.getProxyPort());
        InetSocketAddress realAddr = new InetSocketAddress(mitmClientConfig.getHost(), mitmClientConfig.getPort());

        b.group(workerGroup).channel(NioSocketChannel.class)
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, mitmClientConfig.getConnectTimeoutMillis()) //
                .option(ChannelOption.SO_KEEPALIVE, true) //
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ChannelPipeline pipeline = ch.pipeline();

                        // SOCKS5
                        if (mitmClientConfig.isProxyEnabled() && mitmClientConfig.getProxyType().equals(Proxy.Type.SOCKS)) {
                            pipeline.addLast(new Socks5ProxyHandler(proxyAddr));
                        }
                        // TLS
                        if (mitmClientConfig.isSslEnabled()) {
                            pipeline.addLast(createTrustedClientSslContext().newHandler(ch.alloc(), realAddr.getHostString(), realAddr.getPort()));
                        }
                        // HTTP
                        if (mitmClientConfig.isProxyEnabled() && mitmClientConfig.getProxyType().equals(Proxy.Type.HTTP)) {
                            pipeline.addLast(new HttpProxyHandler(proxyAddr));
                        }
                        pipeline.addLast(new ChannelInboundHandlerAdapter() {
                            @Override
                            public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                                responseHandler.handle(ctx, msg);
                                super.channelRead(ctx, msg);
                            }
                        });
                    }
                });

        ChannelFuture channelFuture = b.connect(realAddr).addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture future) throws Exception {
                if (future.isSuccess()) {
                    // Connection established use handler provided results
                } else {
                    // Close the connection if the connection attempt has failed.
                }
            }
        });

        try {
            this.channel = channelFuture.sync().channel();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

    }

    public void send(String content) {
        send(content.getBytes(StandardCharsets.UTF_8));
    }

    public void send(byte[] content) {
        if (channel == null) {
            throw new IllegalStateException("channel is null");
        }
        try {
            channel.writeAndFlush(Unpooled.copiedBuffer(content)).sync();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private SslContext createTrustedClientSslContext() {
        try {
            SslContextBuilder builder = SslContextBuilder.forClient();
            if (mitmClientConfig.isProxyEnabled()) {
                builder.trustManager(new File(mitmClientConfig.getTrustCrtPath()));
            }
            return builder.build();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public interface ResponseHandler {
        void handle(ChannelHandlerContext ctx, Object msg);
    }
}

/*
 * Copyright 2012 The Netty Project
 *
 * The Netty Project licenses this file to you under the Apache License, version 2.0 (the "License"); you may not use
 * this file except in compliance with the License. You may obtain a copy of the License at:
 *
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */

package com.hyf.proxyserver.server.http;

import com.hyf.proxyserver.server.DirectClientHandler;
import com.hyf.proxyserver.server.util.ProxyUtils;
import com.hyf.proxyserver.server.relay.RelayChannelInitializers;
import com.hyf.proxyserver.server.ssl.SslUtils;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.EncoderException;
import io.netty.handler.codec.base64.Base64;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.FutureListener;
import io.netty.util.concurrent.Promise;

import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static io.netty.handler.codec.http.HttpResponseStatus.BAD_REQUEST;
import static io.netty.handler.codec.http.HttpResponseStatus.FORBIDDEN;

public final class HttpServerConnectHandler extends ChannelInboundHandlerAdapter {

    private final Bootstrap b = new Bootstrap();

    private boolean authorizationRequired = false;

    @Override
    public void channelRead(final ChannelHandlerContext ctx, final Object msg) throws Exception {

        if (!(msg instanceof HttpRequest)) {
            super.channelRead(ctx, msg);
            return;
        }

        HttpRequest request = (HttpRequest) msg;
        if (!(handleConnectRequest(request))) {
            writeProxyAuthenticationRequired(ctx, request);
        }

        String hostHeader = request.headers().get(HttpHeaderNames.HOST);
        if (hostHeader == null) {
            writeErrorAndClose(ctx, request, FORBIDDEN);
            return;
        }

        InetSocketAddress socketAddress = parseHost(hostHeader, ctx);
        ctx.channel().attr(ProxyUtils.TARGET_ADDRESS).setIfAbsent(socketAddress);
        String host = socketAddress.getHostString();
        int port = socketAddress.getPort();

        // HTTP请求不会事先发送CONNECT，HTTPS才会，所以如果是数据报文就先缓存下，后续再转发用
        List<Object> firstMessage = isConnectRequest(request) ? Collections.emptyList() : new ExportHttpRequestEncoder().encode(ctx, request);

        Promise<Channel> promise = ctx.executor().newPromise();
        promise.addListener(new FutureListener<Channel>() {
            @Override
            public void operationComplete(final Future<Channel> future) throws Exception {
                final Channel outboundChannel = future.getNow();
                if (future.isSuccess()) {
                    ChannelFuture responseFuture = isConnectRequest(request) ? ctx.writeAndFlush(new DefaultFullHttpResponse(request.protocolVersion(), HttpResponseStatus.OK))
                            : ctx.newPromise().setSuccess();

                    responseFuture.addListener(new ChannelFutureListener() {
                        @Override
                        public void operationComplete(ChannelFuture channelFuture) {
                            // TODO 报错 NoSuchElementException
                            ctx.pipeline().remove(HttpServerConnectHandler.this);
                            // 仅第一次需要解码获取host用，之后有需要可在RelayHandler后添加
                            ctx.pipeline().remove(HttpRequestDecoder.class.getName());
                            ctx.pipeline().remove(HttpResponseEncoder.class.getName());
                            ctx.pipeline().remove(HttpObjectAggregator.class.getName());
                            outboundChannel.attr(ProxyUtils.TARGET_ADDRESS).setIfAbsent(socketAddress);
                            if (SslUtils.useSsl(ctx)) {
                                outboundChannel.pipeline().addLast(SslUtils.getSslHandlerName(), SslUtils.getClientSslContext().newHandler(ctx.alloc(), host, port));
                            }
                            RelayChannelInitializers.initChannelRelay(ctx.channel(), outboundChannel);
                        }
                    }).addListener(new ChannelFutureListener() {
                        @Override
                        public void operationComplete(ChannelFuture channelFuture) throws Exception {
                            // 表示第一个报文就是数据报文，无CONNECT报文
                            if (!isConnectRequest(request)) {
                                for (Object msg : firstMessage) {
                                    ctx.pipeline().fireChannelRead(msg); // 重新走一遍管道
                                }
                            }
                        }
                    });
                } else {
                    writeErrorAndClose(ctx, request, BAD_REQUEST);
                }
            }
        });

        final Channel inboundChannel = ctx.channel();
        b.group(inboundChannel.eventLoop()) //
                .channel(NioSocketChannel.class) //
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 10000) //
                .option(ChannelOption.SO_KEEPALIVE, false) //
                .handler(new DirectClientHandler(promise)); //

        // TODO 连接管理，不要每次客户端请求都去建立连接，还不关
        b.connect(socketAddress).addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture future) throws Exception {
                if (future.isSuccess()) {
                    // Connection established use handler provided results
                } else {
                    writeErrorAndClose(ctx, request, BAD_REQUEST);
                }
            }
        });

    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ProxyUtils.closeOnFlush(ctx.channel());
    }

    public void setAuthorizationRequired(boolean authorizationRequired) {
        this.authorizationRequired = authorizationRequired;
    }

    private boolean isConnectRequest(HttpRequest request) {
        return request.method() == HttpMethod.CONNECT;
    }

    private boolean handleConnectRequest(HttpRequest request) {

        if (authorizationRequired) {
            // 检查认证头
            String authHeader = request.headers().get(HttpHeaderNames.PROXY_AUTHORIZATION);
            if (authHeader == null || !authenticate(authHeader)) {
                return false;
            }
        }

        return true;
    }

    private boolean authenticate(String authHeader) {
        // 实现Basic Authentication的验证逻辑，如解码凭据、对比数据库等
        // 返回true表示认证通过，false表示认证失败
        // 示例代码仅作示意，实际应替换为具体的认证逻辑
        String credentials = decodeBase64(authHeader.replaceFirst("Basic ", ""));
        String[] parts = credentials.split(":", 2);
        if ("username".equals(parts[0]) && "password".equals(parts[1])) {
            return true;
        }
        return false;
    }

    private String decodeBase64(String encoded) {
        ByteBuf authz = Unpooled.copiedBuffer(encoded, CharsetUtil.UTF_8);
        return Base64.decode(authz).toString(StandardCharsets.UTF_8);
    }

    private void writeProxyAuthenticationRequired(ChannelHandlerContext ctx, HttpRequest request) {
        FullHttpResponse response = new DefaultFullHttpResponse(request.protocolVersion(), HttpResponseStatus.PROXY_AUTHENTICATION_REQUIRED);
        response.headers().set(HttpHeaderNames.PROXY_AUTHENTICATE, "Basic realm=\"MITM\"");
        ctx.writeAndFlush(response);
    }

    private InetSocketAddress parseHost(String hostHeader, ChannelHandlerContext ctx) {
        String host;
        int port;
        int portIdx = hostHeader.lastIndexOf(":");
        if (portIdx != -1) {
            host = hostHeader.substring(0, portIdx);
            port = Integer.parseInt(hostHeader.substring(portIdx + 1));
        } else {
            host = hostHeader;
            if (SslUtils.useSsl(ctx)) {
                port = 443;
            } else {
                port = 80;
            }
        }
        return InetSocketAddress.createUnresolved(host, port);
    }

    private void writeErrorAndClose(ChannelHandlerContext ctx, HttpRequest request, HttpResponseStatus status) {
        ctx.channel().writeAndFlush(new DefaultFullHttpResponse(request.protocolVersion(), status));
        ProxyUtils.closeOnFlush(ctx.channel());
    }

    /**
     * 方便手动编码
     */
    private static class ExportHttpRequestEncoder extends HttpRequestEncoder {

        public List<Object> encode(ChannelHandlerContext ctx, Object msg) {
            try {
                List<Object> out = new ArrayList<>();
                encode(ctx, msg, out);
                return out;
            } catch (EncoderException e) {
                throw e;
            } catch (Throwable t) {
                throw new EncoderException(t);
            }
        }
    }
}

// 八月 06, 2024 3:25:13 下午 io.netty.util.concurrent.DefaultPromise notifyListener0
//         警告: An exception was thrown by com.hyf.proxyserver.server.http.HttpServerConnectHandler$1$2.operationComplete()
//         java.util.NoSuchElementException: com.hyf.proxyserver.server.http.HttpServerConnectHandler
//         at io.netty.channel.DefaultChannelPipeline.getContextOrDie(DefaultChannelPipeline.java:1082)
//         at io.netty.channel.DefaultChannelPipeline.remove(DefaultChannelPipeline.java:417)
//         at com.hyf.proxyserver.server.http.HttpServerConnectHandler$1$2.operationComplete(HttpServerConnectHandler.java:87)
//         at com.hyf.proxyserver.server.http.HttpServerConnectHandler$1$2.operationComplete(HttpServerConnectHandler.java:84)
//         at io.netty.util.concurrent.DefaultPromise.notifyListener0(DefaultPromise.java:590)
//         at io.netty.util.concurrent.DefaultPromise.notifyListenersNow(DefaultPromise.java:557)
//         at io.netty.util.concurrent.DefaultPromise.notifyListeners(DefaultPromise.java:492)
//         at io.netty.util.concurrent.DefaultPromise.addListener(DefaultPromise.java:185)
//         at io.netty.channel.DefaultChannelPromise.addListener(DefaultChannelPromise.java:95)
//         at io.netty.channel.DefaultChannelPromise.addListener(DefaultChannelPromise.java:30)
//         at com.hyf.proxyserver.server.http.HttpServerConnectHandler$1.operationComplete(HttpServerConnectHandler.java:84)
//         at io.netty.util.concurrent.DefaultPromise.notifyListener0(DefaultPromise.java:590)
//         at io.netty.util.concurrent.DefaultPromise.notifyListenersNow(DefaultPromise.java:557)
//         at io.netty.util.concurrent.DefaultPromise.notifyListeners(DefaultPromise.java:492)
//         at io.netty.util.concurrent.DefaultPromise.setValue0(DefaultPromise.java:636)
//         at io.netty.util.concurrent.DefaultPromise.setSuccess0(DefaultPromise.java:625)
//         at io.netty.util.concurrent.DefaultPromise.setSuccess(DefaultPromise.java:97)
//         at com.hyf.proxyserver.server.DirectClientHandler.channelActive(DirectClientHandler.java:34)
//         at io.netty.channel.AbstractChannelHandlerContext.invokeChannelActive(AbstractChannelHandlerContext.java:260)
//         at io.netty.channel.AbstractChannelHandlerContext.invokeChannelActive(AbstractChannelHandlerContext.java:238)
//         at io.netty.channel.AbstractChannelHandlerContext.fireChannelActive(AbstractChannelHandlerContext.java:231)
//         at io.netty.channel.DefaultChannelPipeline$HeadContext.channelActive(DefaultChannelPipeline.java:1398)
//         at io.netty.channel.AbstractChannelHandlerContext.invokeChannelActive(AbstractChannelHandlerContext.java:258)
//         at io.netty.channel.AbstractChannelHandlerContext.invokeChannelActive(AbstractChannelHandlerContext.java:238)
//         at io.netty.channel.DefaultChannelPipeline.fireChannelActive(DefaultChannelPipeline.java:895)
//         at io.netty.channel.nio.AbstractNioChannel$AbstractNioUnsafe.fulfillConnectPromise(AbstractNioChannel.java:306)
//         at io.netty.channel.nio.AbstractNioChannel$AbstractNioUnsafe.finishConnect(AbstractNioChannel.java:336)
//         at io.netty.channel.nio.NioEventLoop.processSelectedKey(NioEventLoop.java:776)
//         at io.netty.channel.nio.NioEventLoop.processSelectedKeysOptimized(NioEventLoop.java:724)
//         at io.netty.channel.nio.NioEventLoop.processSelectedKeys(NioEventLoop.java:650)
//         at io.netty.channel.nio.NioEventLoop.run(NioEventLoop.java:562)
//         at io.netty.util.concurrent.SingleThreadEventExecutor$4.run(SingleThreadEventExecutor.java:997)
//         at io.netty.util.internal.ThreadExecutorMap$2.run(ThreadExecutorMap.java:74)
//         at io.netty.util.concurrent.FastThreadLocalRunnable.run(FastThreadLocalRunnable.java:30)
//         at java.lang.Thread.run(Thread.java:748)

